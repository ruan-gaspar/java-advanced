package com.fiap.commandservice.service;

import com.fiap.commandservice.dto.CommandRequest;
import com.fiap.commandservice.dto.ValidationRequest;
import com.fiap.commandservice.dto.ValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommandService {

    private final RestTemplate restTemplate;

    public CommandService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ValidationResponse sendCommand(CommandRequest request) {
        ValidationRequest validationRequest = new ValidationRequest(request.getCommand());

        ResponseEntity<ValidationResponse> response = restTemplate.postForEntity(
                "http://validator-service/validate",
                validationRequest,
                ValidationResponse.class
        );

        return response.getBody();
    }
}