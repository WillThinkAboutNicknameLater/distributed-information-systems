package ru.nsu.fit.crackhash.manager.service;

import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.manager.exception.RequestNotFoundException;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerResultResponse;
import ru.nsu.fit.crackhash.manager.storage.model.Request;
import ru.nsu.fit.crackhash.manager.storage.repository.ExtendedRequestRepository;
import ru.nsu.fit.crackhash.manager.storage.repository.RequestRepository;

import java.util.UUID;

@Service
public class RequestService {
    private final RequestRepository requestRepository;

    private final ExtendedRequestRepository extendedRequestRepository;

    public RequestService(RequestRepository requestRepository, ExtendedRequestRepository extendedRequestRepository) {
        this.requestRepository = requestRepository;
        this.extendedRequestRepository = extendedRequestRepository;
    }

    public Request getRequest(UUID requestId) throws RequestNotFoundException {
        return requestRepository.findById(requestId)
                                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    public void addWorkerResult(WorkerResultResponse result) throws RequestNotFoundException {
        extendedRequestRepository.addWorkerResult(result);
    }
}
