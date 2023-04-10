package ru.nsu.fit.crackhash.worker.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.worker.queue.message.WorkerResultResponse;
import ru.nsu.fit.crackhash.worker.queue.message.WorkerTaskRequest;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class CrackHashService {
    public WorkerResultResponse crackHash(WorkerTaskRequest workerTask) {
        Set<String> data = new HashSet<>();

        long totalNumberOfCombinations = workerTask.getAlphabet().getSymbols().size();

        long numberOfChecks = 0;

        for (int length = 1; length <= workerTask.getMaxLength(); ++length) {
            long numberOfCombinations = totalNumberOfCombinations / workerTask.getPartCount();
            long numberOfCombinationsToSkip = workerTask.getPartNumber() * numberOfCombinations;
            if (workerTask.getPartNumber() == workerTask.getPartCount() - 1) {
                numberOfCombinations += totalNumberOfCombinations % workerTask.getPartCount();
            }

            log.info("Request #{}. Hash calculation. Part #{}. Range: [{}, {}]",
                     workerTask.getRequestId(),
                     workerTask.getPartNumber(),
                     numberOfCombinationsToSkip,
                     numberOfCombinationsToSkip + numberOfCombinations - 1);

            Generator.permutation(workerTask.getAlphabet().getSymbols().stream().sorted().toList())
                     .withRepetitions(length)
                     .stream()
                     .skip(numberOfCombinationsToSkip)
                     .limit(numberOfCombinations)
                     .forEach(combination -> {
                         String string = String.join("", combination);
                         String hash = DigestUtils.md5Hex(string);

                         if (workerTask.getHash().equals(hash)) {
                             data.add(string);
                         }
                     });

            totalNumberOfCombinations *= workerTask.getAlphabet().getSymbols().size();
            numberOfChecks += numberOfCombinations;
        }

        log.info("Request #{}. Hash calculation. Part #{}. Number of checks: {}",
                 workerTask.getRequestId(),
                 workerTask.getPartNumber(),
                 numberOfChecks);

        return WorkerResultResponse.builder()
                                   .requestId(workerTask.getRequestId())
                                   .partNumber(workerTask.getPartNumber())
                                   .answers(WorkerResultResponse.Answers.builder().words(data).build())
                                   .build();
    }
}
