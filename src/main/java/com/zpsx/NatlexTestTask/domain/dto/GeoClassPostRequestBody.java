package com.zpsx.NatlexTestTask.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class GeoClassPostRequestBody {
    @NotBlank(message = "Geological class code must not be blank.")
    @Size(max = 16, message = "Geological class code length must be below 16 characters.")
    private String code;

    @NotBlank(message = "Geological class name must not be blank.")
    @Size(max = 255, message = "Geological class code length must be below 255 characters.")
    private String name;
}
