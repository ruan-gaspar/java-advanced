package com.fiap.mining_service.controller;

import com.fiap.mining_service.service.MiningCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MiningController {

    private final MiningCommandService miningCommandService;

    public MiningController(MiningCommandService miningCommandService) {
        this.miningCommandService = miningCommandService;
    }

    @GetMapping("/commands/count")
    public ResponseEntity<Map<String, Long>> getCommandCount() {
        return ResponseEntity.ok(miningCommandService.getCommandSummary());
    }
}