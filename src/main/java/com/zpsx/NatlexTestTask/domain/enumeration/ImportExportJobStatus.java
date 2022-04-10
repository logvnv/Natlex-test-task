package com.zpsx.NatlexTestTask.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ImportExportJobStatus {
    DONE,
    ERROR,
    @JsonProperty("IN PROGRESS") IN_PROGRESS
}
