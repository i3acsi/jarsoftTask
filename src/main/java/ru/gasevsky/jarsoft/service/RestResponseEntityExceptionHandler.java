package ru.gasevsky.jarsoft.service;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { PSQLException.class})
    protected ResponseEntity<Object> handleConflict(
            PSQLException ex, WebRequest request) {
        String msg = ex.getLocalizedMessage();
        return handleExceptionInternal(ex, msg,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
