package com.fiap.mining_service.service;

import com.fiap.mining_service.model.CommandCount;
import com.fiap.mining_service.repository.CommandCountRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MiningCommandService {

    private final CommandCountRepository commandCountRepository;

    public MiningCommandService(CommandCountRepository commandCountRepository) {
        this.commandCountRepository = commandCountRepository;
    }

    public void processCommand(String command) {
        System.out.println("Executando comando no robô: " + command);

        CommandCount commandCount = commandCountRepository.findByCommandName(command)
                .orElse(new CommandCount(command, 0L));

        commandCount.setTotalCount(commandCount.getTotalCount() + 1);
        commandCountRepository.save(commandCount);
    }

    public Map<String, Long> getCommandSummary() {
        List<CommandCount> all = commandCountRepository.findAll();

        Map<String, Long> summary = new LinkedHashMap<>();
        for (CommandCount item : all) {
            summary.put(item.getCommandName(), item.getTotalCount());
        }

        return summary;
    }
}