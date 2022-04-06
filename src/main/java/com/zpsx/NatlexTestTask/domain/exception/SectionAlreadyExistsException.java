package com.zpsx.NatlexTestTask.domain.exception;

import com.zpsx.NatlexTestTask.domain.Section;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SectionAlreadyExistsException extends ResponseStatusException {
    public SectionAlreadyExistsException(Section section) {
        super(HttpStatus.BAD_REQUEST, String.format("Section with name '%s' already exists, it's id is '%d'.",
                section.getName(), section.getId()));
    }
}
