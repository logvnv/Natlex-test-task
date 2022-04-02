package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.domain.Section;
import com.zpsx.NatlexTestTask.dto.SectionRequestBody;
import com.zpsx.NatlexTestTask.exception.GeoClassDoesNotExistException;
import com.zpsx.NatlexTestTask.exception.SectionDoesNotExistException;
import com.zpsx.NatlexTestTask.repository.GeoClassRepo;
import com.zpsx.NatlexTestTask.repository.SectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {

    @Autowired
    SectionRepo sectionRepo;
    @Autowired
    GeoClassRepo geoClassRepo;

    public List<Section> readSectionsByGeoCode(String code){
        return sectionRepo.findAllByGeoCode(code);
    }

    public Section createSection(SectionRequestBody sectionRequestBody){
        List<GeoClass> geoClasses = new ArrayList<>();
        for(String code: sectionRequestBody.getGeoCodes()){
            GeoClass geoClass = geoClassRepo.findById(code)
                    .orElseThrow(() -> new GeoClassDoesNotExistException(code));
            geoClasses.add(geoClass);
        }

        Section section = new Section(sectionRequestBody.getName(), geoClasses);
        sectionRepo.save(section);

        return section;
    }

    public Section readSection(long id){
        return sectionRepo.findById(id)
                .orElseThrow(() -> new SectionDoesNotExistException(id));
    }

    public Section updateSection(@RequestBody SectionRequestBody sectionRequestBody){
        Section section = sectionRepo.findById(sectionRequestBody.getId())
                .orElseThrow(() -> new SectionDoesNotExistException(sectionRequestBody.getId()));

        List<GeoClass> geoClasses = new ArrayList<>();
        for(String code: sectionRequestBody.getGeoCodes()){
            GeoClass geoClass = geoClassRepo.findById(code)
                    .orElseThrow(() -> new GeoClassDoesNotExistException(code));

            geoClasses.add(geoClass);
        }

        section.setName(sectionRequestBody.getName());
        section.setGeoClasses(geoClasses);

        sectionRepo.save(section);

        return section;
    }

    public Section deleteSection(long id){
        Section section = sectionRepo.findById(id)
                .orElseThrow(() -> new SectionDoesNotExistException(id));
        sectionRepo.delete(section);

        return section;
    }
}
