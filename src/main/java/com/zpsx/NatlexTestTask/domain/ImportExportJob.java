package com.zpsx.NatlexTestTask.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobStatus;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ImportExportJob {
    @Id @GeneratedValue private Long id;
    @JsonIgnore private ImportExportJobType type;
    @JsonIgnore private Date startedAt;
    private ImportExportJobStatus status;
    private String message;

    @JsonIgnore private static SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_hh_mm");
    @JsonIgnore public String getFileName() {
        return dateFormat.format(startedAt) + "__"  + id.toString() + ".xlsx";
    }

    public ImportExportJob(ImportExportJobType ieJobType){
        this.status = ImportExportJobStatus.IN_PROGRESS;
        this.type = ieJobType;
        this.startedAt = new Date();
    }
}
