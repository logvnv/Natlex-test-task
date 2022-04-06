package com.zpsx.NatlexTestTask.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SectionPostRequestBody {
    @NotBlank(message = "Section name must not be blank.")
    @Size(max = 255, message = "Section name length must be below 255 characters.")
    private String name;

    private List<@Positive(message = "Geological class ID must be a positive number.") Long> geoClasses;

    SectionPostRequestBody(){
        this.geoClasses = new ArrayList<>();
    }
}
