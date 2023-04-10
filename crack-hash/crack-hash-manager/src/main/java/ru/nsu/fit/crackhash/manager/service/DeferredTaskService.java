package ru.nsu.fit.crackhash.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.manager.mapper.TaskMapper;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerTaskRequest;
import ru.nsu.fit.crackhash.manager.queue.producer.WorkerTaskProducer;
import ru.nsu.fit.crackhash.manager.storage.model.DeferredTask;
import ru.nsu.fit.crackhash.manager.storage.repository.DeferredTaskRepository;

@Slf4j
@Service
public class DeferredTaskService {
    private final DeferredTaskRepository deferredTaskRepository;

    private final WorkerTaskProducer workerTaskProducer;

    private final TaskMapper taskMapper;

    public DeferredTaskService(DeferredTaskRepository deferredTaskRepository, WorkerTaskProducer workerTaskProducer, TaskMapper taskMapper) {
        this.deferredTaskRepository = deferredTaskRepository;
        this.workerTaskProducer = workerTaskProducer;
        this.taskMapper = taskMapper;
    }

    public void deferTask(DeferredTask task) {
        log.info("Request #{}. Deferring part #{}", task.getRequestId(), task.getPartNumber());

        deferredTaskRepository.save(task);
    }

    public void distributeTasks() {
        log.info("Distributing tasks");

        for (DeferredTask deferredTask : deferredTaskRepository.findAll()) {
            WorkerTaskRequest workerTaskRequest = taskMapper.map(deferredTask);
            workerTaskProducer.sendTask(workerTaskRequest);
            deferredTaskRepository.delete(deferredTask);
        }
    }
}
