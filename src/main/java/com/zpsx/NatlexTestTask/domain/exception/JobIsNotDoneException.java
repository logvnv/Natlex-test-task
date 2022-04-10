package com.zpsx.NatlexTestTask.domain.exception;

import com.zpsx.NatlexTestTask.domain.ImportExportJob;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JobIsNotDoneException extends ResponseStatusException {
    public JobIsNotDoneException(ImportExportJob ieJob) {
        super(HttpStatus.BAD_REQUEST, buildMessage(ieJob));
    }

    private static String buildMessage(ImportExportJob ieJob){
        if (ieJob.getStatus() == ImportExportJobStatus.IN_PROGRESS)
            return String.format("Export job with id '%s' is not finished yet.", ieJob.getId());
        else
            return String.format("Export job failed with message: '%s'.", ieJob.getMessage());
    }
}
