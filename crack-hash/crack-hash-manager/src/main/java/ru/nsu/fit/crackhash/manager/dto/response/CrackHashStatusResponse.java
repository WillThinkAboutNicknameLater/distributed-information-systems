package ru.nsu.fit.crackhash.manager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CrackHashStatusResponse {
    private CrackHashStatus status;

    private Set<String> data;
}
