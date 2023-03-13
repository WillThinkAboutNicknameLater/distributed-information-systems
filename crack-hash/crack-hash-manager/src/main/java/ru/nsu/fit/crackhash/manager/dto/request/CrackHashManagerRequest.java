
package ru.nsu.fit.crackhash.manager.dto.request;

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
        "partCount",
        "hash",
        "maxLength",
        "alphabet"
})
@XmlRootElement(name = "CrackHashManagerRequest", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request")
public class CrackHashManagerRequest {
    @XmlElement(name = "RequestId", required = true)
    private String requestId;

    @XmlElement(name = "PartNumber", required = true)
    private int partNumber;

    @XmlElement(name = "PartCount", required = true)
    private int partCount;

    @XmlElement(name = "Hash", required = true)
    private String hash;

    @XmlElement(name = "MaxLength", required = true)
    private int maxLength;

    @XmlElement(name = "Alphabet", required = true)
    private Alphabet alphabet;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "symbols"
    })
    public static class Alphabet {
        @XmlElement(name = "Symbols", required = true)
        private List<String> symbols;
    }
}
