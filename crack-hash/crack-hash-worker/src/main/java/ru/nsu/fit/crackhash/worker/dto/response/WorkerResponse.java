
package ru.nsu.fit.crackhash.worker.dto.response;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class WorkerResponse {
    @XmlElement(name = "RequestId", required = true)
    private String requestId;

    @XmlElement(name = "PartNumber", required = true)
    private int partNumber;

    @XmlElement(name = "Answers", required = true)
    private WorkerResponse.Answers answers;

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
        private List<String> words;
    }
}