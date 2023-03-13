package ru.nsu.fit.crackhash.manager.dto.request;

import lombok.Data;

@Data
public class CrackHashRequest {
    private String hash;

    private Integer maxLength;
}
