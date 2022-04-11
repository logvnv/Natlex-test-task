package com.zpsx.NatlexTestTask.service.impl;

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
import com.zpsx.NatlexTestTask.service.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService implements ISectionService {

    @Autowired
    SectionRepo sectionRepo;
    @Autowired
    GeoClassRepo geoClassRepo;

    @Override
    public List<Section> readSectionsByGeoCode(String code){
        return sectionRepo.findAllByGeoCode(code);
    }

    @Override
    public Section readSectionByName(String name) {
        return sectionRepo.findByName(name)
                .orElseThrow(() -> new SectionDoesNotExistException(name));
    }

    @Override
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

    @Override
    public Section readSection(long id){
        return sectionRepo.findById(id)
                .orElseThrow(() -> new SectionDoesNotExistException(id));
    }

    @Override
    public Section updateSection(SectionPutRequestBody sectionPutRequestBody){
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

    @Override
    public Section deleteSection(long id){
        Section section = sectionRepo.findById(id)
                .orElseThrow(() -> new SectionDoesNotExistException(id));
        sectionRepo.delete(section);

        return section;
    }
}
