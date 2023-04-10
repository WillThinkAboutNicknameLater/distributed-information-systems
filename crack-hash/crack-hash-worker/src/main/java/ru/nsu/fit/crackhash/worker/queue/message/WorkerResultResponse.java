
package ru.nsu.fit.crackhash.worker.queue.message;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "requestId",
        "partNumber",
        "answers"
})
@XmlRootElement(name = "CrackHashWorkerResponse", namespace = "http://ccfit.nsu.ru/schema/crack-hash-response")
public class WorkerResultResponse {
    @XmlElement(name = "RequestId", required = true)
    private UUID requestId;

    @XmlElement(name = "PartNumber", required = true)
    private int partNumber;

    @XmlElement(name = "Answers", required = true)
    private Answers answers;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "words"
    })
    public static class Answers {
        @XmlElement(name = "Words", required = true)
        private Set<String> words = new HashSet<>();
    }
}