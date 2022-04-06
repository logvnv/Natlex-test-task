package com.zpsx.NatlexTestTask.domain.exception;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GeoClassAlreadyExistsException extends ResponseStatusException {

    public GeoClassAlreadyExistsException(GeoClass geoClass) {
        super(HttpStatus.BAD_REQUEST,
                String.format("Geological class with code '%s' and name '%s' already exists. It's id is '%d'.",
                        geoClass.getCode(), geoClass.getName(), geoClass.getId()));
    }
}
