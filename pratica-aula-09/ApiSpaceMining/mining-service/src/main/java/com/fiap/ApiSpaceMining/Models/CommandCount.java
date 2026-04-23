package com.fiap.mining_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "command_count")
public class CommandCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "command_name", nullable = false, unique = true)
    private String commandName;

    @Column(name = "total_count", nullable = false)
    private Long totalCount;

    public CommandCount() {
    }

    public CommandCount(String commandName, Long totalCount) {
        this.commandName = commandName;
        this.totalCount = totalCount;
    }

    public Long getId() {
        return id;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}