package com.fiap.commandservice.controller;

import com.fiap.commandservice.dto.CommandRequest;
import com.fiap.commandservice.dto.ValidationResponse;
import com.fiap.commandservice.service.CommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/command")
public class CommandController {

    private final CommandService commandService;

    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public ResponseEntity<ValidationResponse> sendCommand(@RequestBody CommandRequest request) {
        ValidationResponse response = commandService.sendCommand(request);
        return ResponseEntity.ok(response);
    }
}