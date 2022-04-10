package com.zpsx.NatlexTestTask.repository;

import com.zpsx.NatlexTestTask.domain.ImportExportJob;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImportExportJobRepo extends JpaRepository<ImportExportJob, Long> {
    Optional<ImportExportJob> findByIdAndType(Long id, ImportExportJobType type);
}
