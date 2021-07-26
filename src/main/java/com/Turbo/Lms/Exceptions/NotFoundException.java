package com.Turbo.Lms.Exceptions;

public class NotFoundException extends RuntimeException {
    private String messageError;
    public NotFoundException(String message ){
        this.messageError = message;
    }

    public String getMessageError() {
        return messageError;
    }
}
