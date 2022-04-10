package com.zpsx.NatlexTestTask.domain.exception;

import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JobNotFoundException  extends ResponseStatusException {

    public JobNotFoundException(long id, ImportExportJobType type) {
        super(HttpStatus.BAD_REQUEST, String.format("%s job with id '%d' does not exist.", type, id));
    }
}
