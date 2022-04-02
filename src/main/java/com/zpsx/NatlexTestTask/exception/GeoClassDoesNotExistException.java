package com.zpsx.NatlexTestTask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GeoClassDoesNotExistException extends ResponseStatusException {

    public GeoClassDoesNotExistException(String code) {
        super(HttpStatus.BAD_REQUEST, String.format("Geological class with code \"%s\" does not exist.", code));
    }
}
