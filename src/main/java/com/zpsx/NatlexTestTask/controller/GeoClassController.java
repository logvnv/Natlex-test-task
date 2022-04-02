package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.service.GeoClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/geo-classes", produces=MediaType.APPLICATION_JSON_VALUE)
public class GeoClassController {

    @Autowired
    GeoClassService geoClassService;

    @GetMapping
    public ResponseEntity<List<GeoClass>> readAllGeoClass(){
        return new ResponseEntity<>(geoClassService.readAllGeoClass(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoClass> createGeoClass(@RequestBody GeoClass geoClass){
        return new ResponseEntity<>(geoClassService.createGeoClass(geoClass), HttpStatus.CREATED);
    }

    @GetMapping("{code}")
    public ResponseEntity<GeoClass> readGeoClass(@PathVariable String code){
        return new ResponseEntity<>(geoClassService.readGeoClass(code), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoClass> updateGeoClass(@RequestBody GeoClass geoClass){
        return new ResponseEntity<>(geoClassService.updateGeoClass(geoClass), HttpStatus.OK);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<GeoClass> deleteGeoClass(@PathVariable String code){
        return new ResponseEntity<>(geoClassService.deleteGeoClass(code), HttpStatus.OK);
    }
}
