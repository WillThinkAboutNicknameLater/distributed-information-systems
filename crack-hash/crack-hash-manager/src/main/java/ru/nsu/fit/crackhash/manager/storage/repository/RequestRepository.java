package ru.nsu.fit.crackhash.manager.storage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.crackhash.manager.storage.model.Request;

import java.util.UUID;

@Repository
public interface RequestRepository extends MongoRepository<Request, UUID> {
}
