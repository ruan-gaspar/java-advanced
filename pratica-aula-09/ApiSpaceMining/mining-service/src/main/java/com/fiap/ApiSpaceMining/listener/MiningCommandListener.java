package com.fiap.mining_service.listener;

import com.fiap.mining_service.config.RabbitMQConfig;
import com.fiap.mining_service.service.MiningCommandService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MiningCommandListener {

    private final MiningCommandService miningCommandService;

    public MiningCommandListener(MiningCommandService miningCommandService) {
        this.miningCommandService = miningCommandService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveCommand(String command) {
        miningCommandService.processCommand(command);
    }
}