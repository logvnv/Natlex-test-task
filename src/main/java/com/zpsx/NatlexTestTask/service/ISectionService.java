package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.Section;
import com.zpsx.NatlexTestTask.domain.dto.SectionPostRequestBody;
import com.zpsx.NatlexTestTask.domain.dto.SectionPutRequestBody;

import java.util.List;

public interface ISectionService {
    List<Section> readSectionsByGeoCode(String code);
    List<Section> readAll();
    Section readSectionByName(String name);
    Section createSection(SectionPostRequestBody sectionPostRequestBody);
    Section readSection(long id);
    Section updateSection(SectionPutRequestBody sectionPutRequestBody);
    Section deleteSection(long id);
}
