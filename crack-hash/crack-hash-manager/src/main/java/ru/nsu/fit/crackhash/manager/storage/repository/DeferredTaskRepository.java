package ru.nsu.fit.crackhash.manager.storage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.crackhash.manager.storage.model.DeferredTask;

import java.util.UUID;

@Repository
public interface DeferredTaskRepository extends MongoRepository<DeferredTask, UUID> {
}