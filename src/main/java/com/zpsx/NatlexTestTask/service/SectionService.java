package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.domain.Section;
import com.zpsx.NatlexTestTask.domain.dto.SectionPostRequestBody;
import com.zpsx.NatlexTestTask.domain.dto.SectionPutRequestBody;
import com.zpsx.NatlexTestTask.domain.exception.DuplicateGeoClassException;
import com.zpsx.NatlexTestTask.domain.exception.GeoClassDoesNotExistException;
import com.zpsx.NatlexTestTask.domain.exception.SectionAlreadyExistsException;
import com.zpsx.NatlexTestTask.domain.exception.SectionDoesNotExistException;
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

    public Section readSectionsByName(String name) {
        return sectionRepo.findByName(name)
                .orElseThrow(() -> new SectionDoesNotExistException(name));
    }

    public Section createSection(SectionPostRequestBody sectionPostRequestBody){
        sectionRepo.findByName(sectionPostRequestBody.getName())
                .ifPresent(section -> {
                    throw new SectionAlreadyExistsException(section);
                });
        List<GeoClass> geoClasses = new ArrayList<>();
        for(long id: sectionPostRequestBody.getGeoClasses()){
            GeoClass geoClass = geoClassRepo.findById(id)
                    .orElseThrow(() -> new GeoClassDoesNotExistException(id));
            if (geoClasses.contains(geoClass))
                throw new DuplicateGeoClassException(geoClass);
            geoClasses.add(geoClass);
        }

        Section section = new Section(sectionPostRequestBody.getName(), geoClasses);
        sectionRepo.save(section);

        return section;
    }

    public Section readSection(long id){
        return sectionRepo.findById(id)
                .orElseThrow(() -> new SectionDoesNotExistException(id));
    }

    public Section updateSection(@RequestBody SectionPutRequestBody sectionPutRequestBody){
        Section section = sectionRepo.findById(sectionPutRequestBody.getId())
                .orElseThrow(() -> new SectionDoesNotExistException(sectionPutRequestBody.getId()));

        sectionRepo.findByName(sectionPutRequestBody.getName())
                .ifPresent(sameNameSection -> {
                    if (sameNameSection.getId() != sectionPutRequestBody.getId())
                        throw new SectionAlreadyExistsException(sameNameSection);
                });

        List<GeoClass> geoClasses = new ArrayList<>();
        for(long id: sectionPutRequestBody.getGeoClasses()){
            GeoClass geoClass = geoClassRepo.findById(id)
                    .orElseThrow(() -> new GeoClassDoesNotExistException(id));
            if (geoClasses.contains(geoClass))
                throw new DuplicateGeoClassException(geoClass);
            geoClasses.add(geoClass);
        }

        section.setName(sectionPutRequestBody.getName());
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
