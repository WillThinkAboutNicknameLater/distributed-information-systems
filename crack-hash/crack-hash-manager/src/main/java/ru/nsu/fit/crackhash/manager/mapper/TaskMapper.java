package ru.nsu.fit.crackhash.manager.mapper;

import org.mapstruct.Mapper;
import ru.nsu.fit.crackhash.manager.queue.message.WorkerTaskRequest;
import ru.nsu.fit.crackhash.manager.storage.model.DeferredTask;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    WorkerTaskRequest map(DeferredTask task);

    DeferredTask map(WorkerTaskRequest task);
}
