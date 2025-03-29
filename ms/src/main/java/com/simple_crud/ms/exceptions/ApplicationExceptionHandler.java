package com.simple_crud.ms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    private static final String DEFAULT_ERROR_TITLE = "An error occured";
    private static final String DEFAULT_ERROR_MESSAGE = "Try again later";
    private static final String NOT_FOUND_ERROR_TITLE = "Not found";
    private static final String NOT_FOUND_ERROR_MESSAGE = "The specified device does not exist";


    @ExceptionHandler(AppIllegalUserAgentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalDeviceException(final AppIllegalUserAgentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(e.getTitle(), e.getMessage())
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleIllegalDeviceException(final NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(NOT_FOUND_ERROR_TITLE, NOT_FOUND_ERROR_MESSAGE)
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleIllegalDeviceException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(DEFAULT_ERROR_TITLE, DEFAULT_ERROR_MESSAGE)
        );
    }


}
