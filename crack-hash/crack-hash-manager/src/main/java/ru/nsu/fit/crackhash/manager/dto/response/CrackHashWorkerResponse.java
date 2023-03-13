
package ru.nsu.fit.crackhash.manager.dto.response;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "requestId",
        "partNumber",
        "answers"
})
@XmlRootElement(name = "CrackHashWorkerResponse", namespace = "http://ccfit.nsu.ru/schema/crack-hash-response")
public class CrackHashWorkerResponse {
    @XmlElement(name = "RequestId", required = true)
    private String requestId;

    @XmlElement(name = "PartNumber", required = true)
    private int partNumber;

    @XmlElement(name = "Answers", required = true)
    private CrackHashWorkerResponse.Answers answers;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "words"
    })
    public static class Answers {
        @XmlElement(name = "Words", required = true)
        private List<String> words = new ArrayList<>();
    }
}
