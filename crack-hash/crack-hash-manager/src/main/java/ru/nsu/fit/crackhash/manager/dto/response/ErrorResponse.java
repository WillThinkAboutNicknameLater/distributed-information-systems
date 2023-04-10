package ru.nsu.fit.crackhash.manager.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
    private String message;
}
