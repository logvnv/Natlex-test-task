package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.Section;
import com.zpsx.NatlexTestTask.dto.SectionRequestBody;
import com.zpsx.NatlexTestTask.repository.SectionRepo;
import com.zpsx.NatlexTestTask.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/sections")
public class SectionController {

    @Autowired
    SectionRepo sectionRepo;
    @Autowired
    SectionService sectionService;

    @GetMapping("by-code")
    public List<Section> readSectionsByGeoCode(@RequestParam String code){
        return sectionService.readSectionsByGeoCode(code);
    }

    @GetMapping
    public List<Section> readAllSections(){
        return sectionRepo.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Section createSection(@RequestBody SectionRequestBody sectionRequestBody){
        return sectionService.createSection(sectionRequestBody);
    }

    @GetMapping("{id}")
    public Section readSection(@PathVariable long id){
        return sectionService.readSection(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Section updateSection(@RequestBody SectionRequestBody sectionRequestBody){
        return sectionService.updateSection(sectionRequestBody);
    }

    @DeleteMapping("{id}")
    public Section deleteSection(@PathVariable long id){
        return sectionService.deleteSection(id);
    }
}
