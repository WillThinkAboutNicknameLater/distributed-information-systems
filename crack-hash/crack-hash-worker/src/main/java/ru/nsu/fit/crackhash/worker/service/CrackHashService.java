package ru.nsu.fit.crackhash.worker.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.worker.dto.request.CrackHashRequest;
import ru.nsu.fit.crackhash.worker.dto.response.WorkerResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CrackHashService {
    public WorkerResponse crackHash(CrackHashRequest crackHashRequest) {
        List<String> data = new ArrayList<>();

        long totalNumberOfCombinations = crackHashRequest.getAlphabet().getSymbols().size();

        long numberOfChecks = 0;

        for (int length = 1; length <= crackHashRequest.getMaxLength(); ++length) {
            long numberOfCombinations = totalNumberOfCombinations / crackHashRequest.getPartCount();
            long numberOfCombinationsToSkip = crackHashRequest.getPartNumber() * numberOfCombinations;
            if (crackHashRequest.getPartNumber() == crackHashRequest.getPartCount() - 1) {
                numberOfCombinations += totalNumberOfCombinations % crackHashRequest.getPartCount();
            }

            log.info("Worker #{}. Job #{}. Range: [{}, {}]",
                     crackHashRequest.getPartNumber(),
                     crackHashRequest.getRequestId(),
                     numberOfCombinationsToSkip,
                     numberOfCombinationsToSkip + numberOfCombinations - 1);

            Generator.permutation(crackHashRequest.getAlphabet().getSymbols())
                     .withRepetitions(length)
                     .stream()
                     .skip(numberOfCombinationsToSkip)
                     .limit(numberOfCombinations)
                     .forEach(combination -> {
                         String string = String.join("", combination);
                         String hash = DigestUtils.md5Hex(string);

                         if (crackHashRequest.getHash().equals(hash)) {
                             data.add(string);
                         }
                     });

            totalNumberOfCombinations *= crackHashRequest.getAlphabet().getSymbols().size();
            numberOfChecks += numberOfCombinations;
        }

        log.info("Worker #{}. Job #{}. Number of checks: {}",
                 crackHashRequest.getPartNumber(),
                 crackHashRequest.getRequestId(),
                 numberOfChecks);

        return WorkerResponse.builder()
                             .requestId(crackHashRequest.getRequestId())
                             .partNumber(crackHashRequest.getPartNumber())
                             .answers(new WorkerResponse.Answers(data))
                             .build();
    }
}
