package com.zpsx.NatlexTestTask.domain.dto;

import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

@Getter
public class ExportResource {
    private final InputStreamResource resource;
    private final String name;

    public ExportResource(InputStreamResource resource, String name){
        this.resource = resource;
        this.name = name;
    }
}
