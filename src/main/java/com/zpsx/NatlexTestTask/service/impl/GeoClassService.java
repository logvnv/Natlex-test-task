package com.zpsx.NatlexTestTask.service.impl;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPostRequestBody;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPutRequestBody;
import com.zpsx.NatlexTestTask.domain.exception.GeoClassAlreadyExistsException;
import com.zpsx.NatlexTestTask.domain.exception.GeoClassDoesNotExistException;
import com.zpsx.NatlexTestTask.repository.GeoClassRepo;
import com.zpsx.NatlexTestTask.service.IGeoClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoClassService implements IGeoClassService {

    @Autowired
    GeoClassRepo geoClassRepo;

    @Override
    public List<GeoClass> readAllGeoClasses() {
        return geoClassRepo.findAll();
    }

    @Override
    public GeoClass readGeoClassByCodeAndName(String code, String name) {
        return geoClassRepo.findByCodeAndName(code, name)
                .orElseThrow(() -> new GeoClassDoesNotExistException(code, name));
    }

    @Override
    public GeoClass createGeoClass(GeoClassPostRequestBody postBody){
        geoClassRepo.findByCodeAndName(postBody.getCode(), postBody.getName())
                .ifPresent(gc -> {
                    throw new GeoClassAlreadyExistsException(gc);
                });
        return geoClassRepo.save(new GeoClass(postBody));
    }

    @Override
    public GeoClass readGeoClass(long id){
        return geoClassRepo.findById(id)
                .orElseThrow(() -> new GeoClassDoesNotExistException(id));
    }

    @Override
    public GeoClass updateGeoClass(GeoClassPutRequestBody putBody){
        GeoClass geoClass = geoClassRepo.findById(putBody.getId())
                .orElseThrow(() -> new GeoClassDoesNotExistException(putBody.getId()));
        geoClassRepo.findByCodeAndName(putBody.getCode(), putBody.getName())
                .ifPresent(gc -> {
                    if (gc.getId() != geoClass.getId())
                        throw new GeoClassAlreadyExistsException(gc);
                });
        geoClass.setCode(putBody.getCode());
        geoClass.setName(putBody.getName());
        return geoClassRepo.save(geoClass);
    }

    @Override
    public GeoClass deleteGeoClass(long id){
        GeoClass geoClass = geoClassRepo.findById(id)
                .orElseThrow(() -> new GeoClassDoesNotExistException(id));

        geoClassRepo.delete(geoClass);
        return geoClass;
    }
}
