package com.zpsx.NatlexTestTask.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FailedToProcessFileException extends ResponseStatusException {
    public FailedToProcessFileException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process export file. Please try exporting again.");
    }
}
