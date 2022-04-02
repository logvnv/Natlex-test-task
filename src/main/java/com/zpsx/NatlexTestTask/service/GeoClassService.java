package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.repository.GeoClassRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GeoClassService {

    @Autowired
    GeoClassRepo geoClassRepo;

    public GeoClass createGeoClass(GeoClass geoClass){
        if (geoClassRepo.findById(geoClass.getCode()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Geological class with code \"%s\" already exits.", geoClass.getCode()));
        geoClassRepo.save(geoClass);
        return geoClass;
    }

    public GeoClass readGeoClass(String code){
        return geoClassRepo.findById(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("Geological class with code \"%s\" does not exit.", code)));
    }

    public GeoClass updateGeoClass(GeoClass geoClass){
        if (!geoClassRepo.findById(geoClass.getCode()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Geological class with code \"%s\" does not exit.", geoClass.getCode()));

        geoClassRepo.save(geoClass);
        return geoClass;
    }

    public GeoClass deleteGeoClass(String code){
        GeoClass geoClass = geoClassRepo.findById(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("Geological class with code \"%s\" does not exit.", code)));

        geoClassRepo.delete(geoClass);
        return geoClass;
    }

    public List<GeoClass> readAllGeoClass() {
        return geoClassRepo.findAll();
    }
}
