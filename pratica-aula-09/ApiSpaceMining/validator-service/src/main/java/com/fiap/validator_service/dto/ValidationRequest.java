package com.fiap.validator_service.dto;

public class ValidationRequest {

    private String command;

    public ValidationRequest() {
    }

    public ValidationRequest(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}