package com.zpsx.NatlexTestTask.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GeoClassDoesNotExistException extends ResponseStatusException {

    public GeoClassDoesNotExistException(long id) {
        super(HttpStatus.BAD_REQUEST, String.format("Geological class with id '%d' does not exist.", id));
    }

    public GeoClassDoesNotExistException(String code, String name) {
        super(HttpStatus.BAD_REQUEST, String.format("Geological class with code '%s' and name '%s' does not exist.",
                code, name));
    }
}
