package ru.nsu.fit.crackhash.manager.storage.repository;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.crackhash.manager.exception.RequestNotFoundException;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerResultResponse;
import ru.nsu.fit.crackhash.manager.storage.model.Request;
import ru.nsu.fit.crackhash.manager.storage.model.WorkerResult;
import ru.nsu.fit.crackhash.manager.storage.model.RequestStatus;

import java.util.Optional;

@Repository
public class ExtendedRequestRepository {
    private final RequestRepository requestRepository;

    private final MongoTemplate mongoTemplate;

    public ExtendedRequestRepository(RequestRepository requestRepository, MongoTemplate mongoTemplate) {
        this.requestRepository = requestRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void addWorkerResult(WorkerResultResponse workerResult) throws RequestNotFoundException {
        WorkerResult result = WorkerResult.builder()
                                          .partNumber(workerResult.getPartNumber())
                                          .data(workerResult.getAnswers().getWords())
                                          .build();

        Query filter = Query.query(Criteria.where("_id").is(workerResult.getRequestId()));
        Update update = new Update().addToSet("results", result);
        Request request = Optional.ofNullable(mongoTemplate.findAndModify(filter,
                                                                          update,
                                                                          FindAndModifyOptions.options().returnNew(true),
                                                                          Request.class))
                                  .orElseThrow(() -> new RequestNotFoundException(workerResult.getRequestId()));

        if (request.getResults().size() == request.getPartCount()) {
            request.setStatus(RequestStatus.READY);
            requestRepository.save(request);
        }
    }
}
