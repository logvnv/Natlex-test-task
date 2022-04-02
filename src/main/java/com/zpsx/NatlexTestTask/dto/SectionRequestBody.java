package com.zpsx.NatlexTestTask.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SectionRequestBody {
    private long id;
    private String name;
    private List<String> geoCodes;
}
