package com.zpsx.NatlexTestTask.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class GeoClassPutRequestBody extends GeoClassPostRequestBody {
    @NotNull(message = "You must specify geological class id.")
    @Positive(message = "Geological class id must be a positive number.")
    private Long id;
}
