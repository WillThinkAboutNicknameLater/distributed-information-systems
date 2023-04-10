package ru.nsu.fit.crackhash.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.crackhash.manager.dto.request.CrackHashRequest;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashResponse;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashStatusResponse;
import ru.nsu.fit.crackhash.manager.service.HashService;

import java.util.UUID;

@RequestMapping("/api/hash")
@RestController
public class HashController {
    private final HashService hashService;

    public HashController(HashService hashService) {
        this.hashService = hashService;
    }

    @PostMapping("/crack")
    public ResponseEntity<CrackHashResponse> crackHash(@RequestBody CrackHashRequest crackHashRequest) {
        CrackHashResponse response = hashService.crackHash(crackHashRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/status")
    public ResponseEntity<CrackHashStatusResponse> getStatus(@RequestParam("request-id") UUID requestId) {
        CrackHashStatusResponse response = hashService.getStatus(requestId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
