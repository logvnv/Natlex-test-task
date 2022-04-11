package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.ImportExportJob;
import com.zpsx.NatlexTestTask.domain.dto.ExportResource;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobType;
import org.springframework.web.multipart.MultipartFile;

public interface IImportExportService {
    long importFile(MultipartFile file);
    long exportFile();
    ImportExportJob getJob(long id, ImportExportJobType type);
    ExportResource getExportResource(Long id);
}
