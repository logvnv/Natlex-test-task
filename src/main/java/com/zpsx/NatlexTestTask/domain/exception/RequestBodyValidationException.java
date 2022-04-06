package com.zpsx.NatlexTestTask.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class RequestBodyValidationException extends ResponseStatusException {

    public RequestBodyValidationException(List<ObjectError> errors) {
        super(HttpStatus.BAD_REQUEST, errorsToMessage(errors));
    }

    static private String errorsToMessage(List<ObjectError> errors){
        StringBuilder message = new StringBuilder();
        for (ObjectError error: errors){
            if (message.length() != 0)
                message.append("\n");
            message.append(error.getDefaultMessage());
        }
        return message.toString();
    }
}
