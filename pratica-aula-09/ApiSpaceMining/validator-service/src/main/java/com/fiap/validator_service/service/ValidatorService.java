package com.fiap.validator_service.service;

import com.fiap.validator_service.config.RabbitMQConfig;
import com.fiap.validator_service.dto.ValidationRequest;
import com.fiap.validator_service.dto.ValidationResponse;
import com.fiap.validator_service.exception.InvalidCommandException;
import com.fiap.validator_service.exception.SimulatedCommunicationException;
import com.fiap.validator_service.model.MiningCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ValidatorService {

    private final RabbitTemplate rabbitTemplate;

    public ValidatorService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Retryable(
            retryFor = SimulatedCommunicationException.class,
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public ValidationResponse validateAndPublish(ValidationRequest request) {
        String normalizedCommand = normalizeAndValidate(request.getCommand());

        simulateRandomFailure();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                normalizedCommand
        );

        return new ValidationResponse(
                "SUCCESS",
                "Comando validado e enviado para a fila com sucesso.",
                normalizedCommand
        );
    }

    @Recover
    public ValidationResponse recover(SimulatedCommunicationException ex, ValidationRequest request) {
        throw new SimulatedCommunicationException(
                "Falha na comunicação após múltiplas tentativas com exponential backoff."
        );
    }

    private String normalizeAndValidate(String command) {
        if (command == null || command.isBlank()) {
            throw new InvalidCommandException("O campo command é obrigatório.");
        }

        String normalized = command.trim().toUpperCase();

        try {
            MiningCommand.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new InvalidCommandException(
                    "Comando inválido. Valores aceitos: RIGHT, LEFT, FRONT, BACK, OPEN, CLOSE."
            );
        }

        return normalized;
    }

    private void simulateRandomFailure() {
        boolean fail = ThreadLocalRandom.current().nextBoolean();

        if (fail) {
            throw new SimulatedCommunicationException("Falha de comunicação simulada.");
        }
    }
}