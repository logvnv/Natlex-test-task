package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.ImportExportJob;
import com.zpsx.NatlexTestTask.domain.dto.ExportResource;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobType;
import com.zpsx.NatlexTestTask.service.ImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api")
public class ImportExportController {

    @Autowired
    ImportExportService ieService;

    @PostMapping(path = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public long importFile(@RequestParam("file") MultipartFile file){
        return ieService.importFile(file);
    }

    @GetMapping("import/{id}")
    public ImportExportJob getImportJob(@PathVariable("id") long id){
        return ieService.getJob(id, ImportExportJobType.IMPORT);
    }

    @GetMapping("export")
    public long exportFile(){
        return ieService.exportFile();
    }

    @GetMapping("export/{id}")
    public ImportExportJob getExportJob(@PathVariable("id") long id){
        return ieService.getJob(id, ImportExportJobType.EXPORT);
    }

    @GetMapping("export/{id}/file")
    public ResponseEntity<Resource> getExportFile(@PathVariable("id") long id){
        ExportResource exportResource = ieService.getExportResource(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + exportResource.getName())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(exportResource.getResource());
    }
}
