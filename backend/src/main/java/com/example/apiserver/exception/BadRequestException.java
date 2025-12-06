package com.example.apiserver.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private Object data;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message, Object data) {
        super(message);
        this.data = data;
    }
}

