package ru.nsu.fit.crackhash.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.crackhash.manager.dto.request.CrackHashRequest;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashResponse;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashStatusResponse;
import ru.nsu.fit.crackhash.manager.service.ManagerService;

import java.util.UUID;

@RequestMapping("/api/hash")
@RestController
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/crack")
    public ResponseEntity<CrackHashResponse> crackHash(@RequestBody CrackHashRequest crackHashRequest) {
        CrackHashResponse response = managerService.crackHash(crackHashRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<CrackHashStatusResponse> getStatus(@RequestParam UUID requestId) {
        CrackHashStatusResponse response = managerService.getStatus(requestId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
