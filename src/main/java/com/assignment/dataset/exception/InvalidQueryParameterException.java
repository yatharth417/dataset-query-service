package com.assignment.dataset.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQueryParameterException extends DatasetException {
    public InvalidQueryParameterException(String message) {
        super(message);
    }
}