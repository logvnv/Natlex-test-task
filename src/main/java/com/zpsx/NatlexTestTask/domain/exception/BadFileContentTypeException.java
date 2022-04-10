package com.zpsx.NatlexTestTask.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadFileContentTypeException extends ResponseStatusException {
    public BadFileContentTypeException(String contentType){
        super(HttpStatus.BAD_REQUEST, String.format("Bad file content type. Expected " +
                "'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' " +
                "but was '%s'.", contentType));
    }
}
