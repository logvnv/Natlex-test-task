package com.zpsx.NatlexTestTask.domain.exception;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateGeoClassException extends ResponseStatusException {
    public DuplicateGeoClassException(GeoClass geoClass){
        super(HttpStatus.BAD_REQUEST, String.format("Multiple occurrence of geological class with id '%d'.",
                geoClass.getId()));
    }
}
