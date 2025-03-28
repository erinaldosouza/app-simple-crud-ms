package com.simple_crud.ms.exceptions;

import lombok.Getter;

@Getter
public class AppIllegalUserAgentException extends RuntimeException {

    private final String title;

    public AppIllegalUserAgentException(String title, String message) {
        this(title, message, null);
    }

    public AppIllegalUserAgentException(String title, String message, Throwable cause) {
        super(message, cause);
        this.title = title;
    }

}
