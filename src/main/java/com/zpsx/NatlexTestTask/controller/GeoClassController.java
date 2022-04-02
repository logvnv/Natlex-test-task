package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.service.GeoClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/geo-classes")
public class GeoClassController {

    @Autowired
    GeoClassService geoClassService;

    @GetMapping
    public List<GeoClass> readAllGeoClass(){
        return geoClassService.readAllGeoClass();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GeoClass createGeoClass(@RequestBody GeoClass geoClass){
        return geoClassService.createGeoClass(geoClass);
    }

    @GetMapping("{code}")
    public GeoClass readGeoClass(@PathVariable String code){
        return geoClassService.readGeoClass(code);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GeoClass updateGeoClass(@RequestBody GeoClass geoClass){
        return geoClassService.updateGeoClass(geoClass);
    }

    @DeleteMapping("{code}")
    public GeoClass deleteGeoClass(@PathVariable String code){
        return geoClassService.deleteGeoClass(code);
    }
}
