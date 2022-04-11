package com.zpsx.NatlexTestTask;

import com.zpsx.NatlexTestTask.domain.ImportExportJob;
import com.zpsx.NatlexTestTask.domain.enumeration.ImportExportJobStatus;
import com.zpsx.NatlexTestTask.repository.ImportExportJobRepo;
import com.zpsx.NatlexTestTask.service.helper.ImportExportHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@ActiveProfiles(profiles = "non-async")
public class ImportExportHelperTest {

    @Autowired
    ImportExportHelper importExportHelper;

    @MockBean
    private ImportExportJobRepo importExportJobRepo;

    private final ImportExportJob ioJob = Mockito.mock(ImportExportJob.class);

    @Test
    @Sql(value = {"/query/section_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/query/section_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createFileDoneTest(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MM_dd_hh_mm__ss");
        when(ioJob.getFileName()).thenReturn("test" + dateFormat.format(new Date()) + ".xlsx");

        importExportHelper.createFile(ioJob);

        Mockito.verify(ioJob, Mockito.times(1)).setStatus(ImportExportJobStatus.DONE);
        Mockito.verify(importExportJobRepo, Mockito.only()).save(ioJob);
    }

    @Test
    @Sql(value = {"/query/section_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/query/section_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void parseFileMultiTest() throws IOException {
        Resource fileResource = new ClassPathResource("/table/multi_test.xlsx");
        importExportHelper.importFile(fileResource.getInputStream(), ioJob);

        Mockito.verify(ioJob, Mockito.times(1)).setStatus(ImportExportJobStatus.DONE);
        Mockito.verify(importExportJobRepo, Mockito.only()).save(ioJob);
    }

    @Test
    public void parseFileDoneTest() throws IOException {
        Resource fileResource = new ClassPathResource("/table/sample.xlsx");
        importExportHelper.importFile(fileResource.getInputStream(), ioJob);

        Mockito.verify(ioJob, Mockito.only()).setStatus(ImportExportJobStatus.DONE);
        Mockito.verify(importExportJobRepo, Mockito.only()).save(ioJob);
    }

    @Test
    public void parseFileEvenNColsTest() throws IOException {
        Resource fileResource = new ClassPathResource("/table/even_n_cols.xlsx");
        importExportHelper.importFile(fileResource.getInputStream(), ioJob);

        Mockito.verify(ioJob, Mockito.times(1))
                .setMessage("Table must have odd number of columns [1, 5].");
        Mockito.verify(ioJob, Mockito.times(1)).setStatus(ImportExportJobStatus.ERROR);
        Mockito.verify(importExportJobRepo, Mockito.only()).save(ioJob);
    }

    @Test
    public void parseFileMissingCodeTest() throws IOException {
        Resource fileResource = new ClassPathResource("/table/missing_code.xlsx");
        importExportHelper.importFile(fileResource.getInputStream(), ioJob);

        Mockito.verify(ioJob, Mockito.times(1))
                .setMessage("Corresponding geological class code and name " +
                                "must either be filled or not both [2, 4].");
        Mockito.verify(ioJob, Mockito.times(1)).setStatus(ImportExportJobStatus.ERROR);
        Mockito.verify(importExportJobRepo, Mockito.only()).save(ioJob);
    }

    @Test
    public void parseFileDuplicateSectionNameTest() throws IOException {
        Resource fileResource = new ClassPathResource("/table/dup_section_name.xlsx");
        importExportHelper.importFile(fileResource.getInputStream(), ioJob);

        Mockito.verify(ioJob, Mockito.times(1))
                .setMessage("Table contains duplicate section name [3, 1].");
        Mockito.verify(ioJob, Mockito.times(1)).setStatus(ImportExportJobStatus.ERROR);
        Mockito.verify(importExportJobRepo, Mockito.only()).save(ioJob);
    }

    @Test
    public void parseFileDuplicateGeoClassTest() throws IOException {
        Resource fileResource = new ClassPathResource("/table/dup_geo_class.xlsx");
        importExportHelper.importFile(fileResource.getInputStream(), ioJob);

        Mockito.verify(ioJob, Mockito.times(1))
                .setMessage("Section must not have duplicate geological classes [2, 5].");
        Mockito.verify(ioJob, Mockito.times(1)).setStatus(ImportExportJobStatus.ERROR);
        Mockito.verify(importExportJobRepo, Mockito.only()).save(ioJob);
    }
}
