package ru.nsu.fit.crackhash.worker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.crackhash.worker.dto.request.CrackHashRequest;
import ru.nsu.fit.crackhash.worker.service.WorkerService;

@RequestMapping("/internal/api/worker/hash/crack")
@RestController
public class WorkerController {
    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping(value = "/task", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Void> crackHash(@RequestBody CrackHashRequest crackHashRequest) {
        workerService.crackHash(crackHashRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
