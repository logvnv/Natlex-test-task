package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPostRequestBody;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPutRequestBody;
import com.zpsx.NatlexTestTask.domain.exception.GeoClassAlreadyExistsException;
import com.zpsx.NatlexTestTask.domain.exception.GeoClassDoesNotExistException;
import com.zpsx.NatlexTestTask.repository.GeoClassRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoClassService {

    @Autowired
    GeoClassRepo geoClassRepo;

    public List<GeoClass> readAllGeoClass() {
        return geoClassRepo.findAll();
    }

    public GeoClass readGeoClassByCodeAndName(String code, String name) {
        return geoClassRepo.findByCodeAndName(code, name)
                .orElseThrow(() -> new GeoClassDoesNotExistException(code, name));
    }

    public GeoClass createGeoClass(GeoClassPostRequestBody postBody){
        geoClassRepo.findByCodeAndName(postBody.getCode(), postBody.getName())
                .ifPresent(gc -> {
                    throw new GeoClassAlreadyExistsException(gc);
                });
        return geoClassRepo.save(new GeoClass(postBody));
    }

    public GeoClass readGeoClass(long id){
        return geoClassRepo.findById(id)
                .orElseThrow(() -> new GeoClassDoesNotExistException(id));
    }

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

    public GeoClass deleteGeoClass(long id){
        GeoClass geoClass = geoClassRepo.findById(id)
                .orElseThrow(() -> new GeoClassDoesNotExistException(id));

        geoClassRepo.delete(geoClass);
        return geoClass;
    }
}
