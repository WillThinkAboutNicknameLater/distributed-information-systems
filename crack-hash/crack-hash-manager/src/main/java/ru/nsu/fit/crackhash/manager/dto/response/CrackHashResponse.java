package ru.nsu.fit.crackhash.manager.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class CrackHashResponse {
    private UUID requestId;
}
