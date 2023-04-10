package ru.nsu.fit.crackhash.manager.storage.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("requests")
public class Request {
    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();

    private int partCount;

    @Builder.Default
    private RequestStatus status = RequestStatus.IN_PROGRESS;

    @Builder.Default
    private Set<WorkerResult> results = new HashSet<>();

    public Set<String> collectAllResults() {
        return getResults().stream()
                           .map(WorkerResult::getData)
                           .reduce(new HashSet<>(), (resultSet, newSet) -> {
                               resultSet.addAll(newSet);
                               return resultSet;
                           });
    }
}
