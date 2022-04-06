package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.GeoClass;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPostRequestBody;
import com.zpsx.NatlexTestTask.domain.dto.GeoClassPutRequestBody;
import com.zpsx.NatlexTestTask.domain.exception.RequestBodyValidationException;
import com.zpsx.NatlexTestTask.service.GeoClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/geo-classes")
public class GeoClassController {

    @Autowired
    GeoClassService geoClassService;

    @GetMapping
    public List<GeoClass> readAllGeoClass(){
        return geoClassService.readAllGeoClass();
    }

    @GetMapping("by-code-and-name")
    public GeoClass readGeoClassByCodeAndName(@RequestParam String code, @RequestParam String name){
        return geoClassService.readGeoClassByCodeAndName(code, name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public GeoClass createGeoClass(@RequestBody @Valid GeoClassPostRequestBody geoClass, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new RequestBodyValidationException(bindingResult.getAllErrors());
        return geoClassService.createGeoClass(geoClass);
    }

    @GetMapping("{id}")
    public GeoClass readGeoClass(@PathVariable long id){
        return geoClassService.readGeoClass(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GeoClass updateGeoClass(@RequestBody @Valid GeoClassPutRequestBody geoClass, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new RequestBodyValidationException(bindingResult.getAllErrors());
        return geoClassService.updateGeoClass(geoClass);
    }

    @DeleteMapping("{id}")
    public GeoClass deleteGeoClass(@PathVariable long id){
        return geoClassService.deleteGeoClass(id);
    }
}
