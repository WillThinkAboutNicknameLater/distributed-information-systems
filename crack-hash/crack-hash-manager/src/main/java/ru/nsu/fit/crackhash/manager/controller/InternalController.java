package ru.nsu.fit.crackhash.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.crackhash.manager.dto.response.CrackHashWorkerResponse;
import ru.nsu.fit.crackhash.manager.service.ManagerService;

@RequestMapping("/internal/api")
@RestController
public class InternalController {
    private final ManagerService managerService;

    @Autowired
    public InternalController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PatchMapping(value = "/manager/hash/crack/request", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Void> receiveWorkerResponse(@RequestBody CrackHashWorkerResponse workerResponse) {
        managerService.receiveWorkerResponse(workerResponse);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
