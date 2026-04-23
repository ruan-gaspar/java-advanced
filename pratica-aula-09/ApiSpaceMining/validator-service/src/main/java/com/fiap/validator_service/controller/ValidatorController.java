package com.fiap.validator_service.controller;

import com.fiap.validator_service.dto.ValidationRequest;
import com.fiap.validator_service.dto.ValidationResponse;
import com.fiap.validator_service.service.ValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidatorController {

    private final ValidatorService validatorService;

    public ValidatorController(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @PostMapping
    public ResponseEntity<ValidationResponse> validate(@RequestBody ValidationRequest request) {
        ValidationResponse response = validatorService.validateAndPublish(request);
        return ResponseEntity.ok(response);
    }
}