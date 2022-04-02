package com.zpsx.NatlexTestTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GeoClassAlreadyExistsException extends ResponseStatusException {
    public GeoClassAlreadyExistsException(String code) {
        super(HttpStatus.BAD_REQUEST, String.format("Geological class with code \"%s\" already exists.", code));
    }
}
