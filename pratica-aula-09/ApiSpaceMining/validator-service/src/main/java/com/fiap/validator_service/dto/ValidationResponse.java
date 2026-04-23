package com.fiap.validator_service.dto;

public class ValidationResponse {

    private String status;
    private String message;
    private String command;

    public ValidationResponse() {
    }

    public ValidationResponse(String status, String message, String command) {
        this.status = status;
        this.message = message;
        this.command = command;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}