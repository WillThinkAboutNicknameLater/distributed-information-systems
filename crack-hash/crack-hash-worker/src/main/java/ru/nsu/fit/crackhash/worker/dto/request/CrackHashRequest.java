
package ru.nsu.fit.crackhash.worker.dto.request;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

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
public class CrackHashRequest {
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
