package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.exception.GeoClassAlreadyExistsException;
import com.zpsx.NatlexTestTask.exception.GeoClassDoesNotExistException;
import com.zpsx.NatlexTestTask.repository.GeoClassRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoClassService {

    @Autowired
    GeoClassRepo geoClassRepo;

    public GeoClass createGeoClass(GeoClass geoClass){
        if (geoClassRepo.findById(geoClass.getCode()).isPresent())
            throw new GeoClassAlreadyExistsException(geoClass.getCode());
        geoClassRepo.save(geoClass);
        return geoClass;
    }

    public GeoClass readGeoClass(String code){
        return geoClassRepo.findById(code)
                .orElseThrow(() -> new GeoClassDoesNotExistException(code));
    }

    public GeoClass updateGeoClass(GeoClass geoClass){
        if (!geoClassRepo.findById(geoClass.getCode()).isPresent())
            throw new GeoClassDoesNotExistException(geoClass.getCode());

        geoClassRepo.save(geoClass);
        return geoClass;
    }

    public GeoClass deleteGeoClass(String code){
        GeoClass geoClass = geoClassRepo.findById(code)
                .orElseThrow(() -> new GeoClassDoesNotExistException(code));

        geoClassRepo.delete(geoClass);
        return geoClass;
    }

    public List<GeoClass> readAllGeoClass() {
        return geoClassRepo.findAll();
    }
}
