package ru.nsu.fit.crackhash.manager.storage.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("deferred-tasks")
public class DeferredTask {
    @Builder.Default
    @Id
    private UUID id = UUID.randomUUID();
    
    private UUID requestId;

    private int partNumber;

    private int partCount;

    private String hash;

    private int maxLength;

    private Alphabet alphabet;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Alphabet {
        private Set<String> symbols;
    }
}
