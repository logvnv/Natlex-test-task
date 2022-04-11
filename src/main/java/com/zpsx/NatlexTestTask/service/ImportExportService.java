package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.ImportExportJob;
import com.zpsx.NatlexTestTask.domain.dto.ExportResource;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobStatus;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobType;
import com.zpsx.NatlexTestTask.domain.exception.BadFileContentTypeException;
import com.zpsx.NatlexTestTask.domain.exception.FailedToProcessFileException;
import com.zpsx.NatlexTestTask.domain.exception.JobIsNotDoneException;
import com.zpsx.NatlexTestTask.domain.exception.JobNotFoundException;
import com.zpsx.NatlexTestTask.repository.ImportExportJobRepo;
import com.zpsx.NatlexTestTask.service.helper.ImportExportHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class ImportExportService {

    @Value("${export-dir}")
    private String exportDir;

    @Autowired
    ImportExportJobRepo importExportJobRepo;
    @Autowired
    ImportExportHelper importExportHelper;

    public long importFile(MultipartFile file){
        if (!Objects.equals(file.getContentType(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            throw new BadFileContentTypeException(file.getContentType());

        ImportExportJob importJob = new ImportExportJob(ImportExportJobType.IMPORT);
        importExportJobRepo.save(importJob);

        try {
            importExportHelper.importFile(file.getInputStream(), importJob);
        }
        catch (IOException e){
            importExportJobRepo.delete(importJob);
            throw new FailedToProcessFileException();
        }

        return importJob.getId();
    }

    public long exportFile() {
        ImportExportJob importJob = new ImportExportJob(ImportExportJobType.EXPORT);
        importExportJobRepo.save(importJob);

        importExportHelper.createFile(importJob);

        return importJob.getId();
    }

    public ImportExportJob getJob(long id, ImportExportJobType type) {
        return importExportJobRepo.findByIdAndType(id, type)
                .orElseThrow(() -> new JobNotFoundException(id, type));
    }

    public ExportResource getExportResource(Long id) {
        ImportExportJob exportJob = importExportJobRepo.findByIdAndType(id, ImportExportJobType.EXPORT)
                .orElseThrow(() -> new JobNotFoundException(id, ImportExportJobType.EXPORT));
        if (exportJob.getStatus() != ImportExportJobStatus.DONE)
            throw new JobIsNotDoneException(exportJob);

        try {
            InputStreamResource resource = new InputStreamResource(
                    new ByteArrayInputStream(
                            Files.readAllBytes(
                                    new File(exportDir + "/" + exportJob.getFileName()).toPath())));

            return new ExportResource(resource, exportJob.getFileName());
        }
        catch (IOException e){
            throw new FailedToProcessFileException();
        }
    }
}
