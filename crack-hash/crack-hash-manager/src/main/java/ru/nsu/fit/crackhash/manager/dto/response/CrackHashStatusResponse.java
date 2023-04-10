package ru.nsu.fit.crackhash.manager.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.nsu.fit.crackhash.manager.storage.model.RequestStatus;

import java.util.Set;

@Builder
@Data
public class CrackHashStatusResponse {
    private RequestStatus status;

    private Set<String> data;
}
