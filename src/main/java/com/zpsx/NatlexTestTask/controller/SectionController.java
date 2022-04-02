package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.Section;
import com.zpsx.NatlexTestTask.dto.SectionRequestBody;
import com.zpsx.NatlexTestTask.repository.SectionRepo;
import com.zpsx.NatlexTestTask.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/sections", produces=MediaType.APPLICATION_JSON_VALUE)
public class SectionController {

    @Autowired
    SectionRepo sectionRepo;
    @Autowired
    SectionService sectionService;

    @GetMapping("by-code")
    public ResponseEntity<List<Section>> readSectionsByGeoCode(@RequestParam String code){
        return new ResponseEntity<>(sectionService.readSectionsByGeoCode(code), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Section>> readAllSections(){
        return new ResponseEntity<>(sectionRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Section> createSection(@RequestBody SectionRequestBody sectionRequestBody){
        return new ResponseEntity<>(sectionService.createSection(sectionRequestBody), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Section> readSection(@PathVariable long id){
        return new ResponseEntity<>(sectionService.readSection(id), HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Section> updateSection(@RequestBody SectionRequestBody sectionRequestBody){
        return new ResponseEntity<>(sectionService.updateSection(sectionRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Section> deleteSection(@PathVariable long id){
        return new ResponseEntity<>(sectionService.deleteSection(id), HttpStatus.OK);
    }
}
