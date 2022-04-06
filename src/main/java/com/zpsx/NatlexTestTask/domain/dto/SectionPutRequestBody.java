package com.zpsx.NatlexTestTask.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class SectionPutRequestBody extends SectionPostRequestBody{
    @NotNull(message = "You must specify section id.")
    @Positive(message = "Section id must be a positive number.")
    private Long id;
}
