package ru.nsu.fit.crackhash.manager.storage.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerResult {
    private int partNumber;

    @Builder.Default
    private Set<String> data = new HashSet<>();
}