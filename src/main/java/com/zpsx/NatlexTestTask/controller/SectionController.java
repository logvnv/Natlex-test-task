package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.Section;
import com.zpsx.NatlexTestTask.domain.dto.SectionPostRequestBody;
import com.zpsx.NatlexTestTask.domain.dto.SectionPutRequestBody;
import com.zpsx.NatlexTestTask.domain.exception.RequestBodyValidationException;
import com.zpsx.NatlexTestTask.repository.SectionRepo;
import com.zpsx.NatlexTestTask.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/sections")
public class SectionController {

    @Autowired
    SectionRepo sectionRepo;
    @Autowired
    SectionService sectionService;

    @GetMapping("by-code")
    public List<Section> readSectionsByGeoCode(@RequestParam String code){
        return sectionService.readSectionsByGeoCode(code);
    }

    @GetMapping("by-name")
    public Section readSectionByName(@RequestParam String name){
        return sectionService.readSectionsByName(name);
    }

    @GetMapping
    public List<Section> readAllSections(){
        return sectionRepo.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Section createSection(@RequestBody @Valid SectionPostRequestBody sectionPostRequestBody,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new RequestBodyValidationException(bindingResult.getAllErrors());
        return sectionService.createSection(sectionPostRequestBody);
    }

    @GetMapping("{id}")
    public Section readSection(@PathVariable long id){
        return sectionService.readSection(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Section updateSection(@RequestBody SectionPutRequestBody sectionPutRequestBody, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new RequestBodyValidationException(bindingResult.getAllErrors());
        return sectionService.updateSection(sectionPutRequestBody);
    }

    @DeleteMapping("{id}")
    public Section deleteSection(@PathVariable long id){
        return sectionService.deleteSection(id);
    }
}
