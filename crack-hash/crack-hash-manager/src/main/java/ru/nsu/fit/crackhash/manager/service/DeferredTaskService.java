package ru.nsu.fit.crackhash.manager.service;

import org.springframework.stereotype.Service;
import ru.nsu.fit.crackhash.manager.mapper.TaskMapper;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerTaskRequest;
import ru.nsu.fit.crackhash.manager.queue.producer.WorkerTaskProducer;
import ru.nsu.fit.crackhash.manager.storage.model.DeferredTask;
import ru.nsu.fit.crackhash.manager.storage.repository.DeferredTaskRepository;

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
        deferredTaskRepository.save(task);
    }

    public void distributeTasks() {
        for (DeferredTask deferredTask : deferredTaskRepository.findAll()) {
            WorkerTaskRequest workerTaskRequest = taskMapper.map(deferredTask);
            workerTaskProducer.sendTask(workerTaskRequest);
            deferredTaskRepository.delete(deferredTask);
        }
    }
}
