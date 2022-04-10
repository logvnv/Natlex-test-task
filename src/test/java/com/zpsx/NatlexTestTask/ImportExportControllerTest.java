package com.zpsx.NatlexTestTask;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource("/application-test.properties")
@Sql(value = {"/query/ie_job_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/query/ie_job_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ImportExportControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    public void exportTest() throws Exception {
        String result = mockMvc.perform(get("/api/export"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(Long.parseLong(result)).isEqualTo(10);
    }

    @Test
    public void importTest() throws Exception {
        Resource fileResource = new ClassPathResource("/table/sample.xlsx");
        MockMultipartFile file = new MockMultipartFile("file",
                null,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                fileResource.getInputStream());

        String result = mockMvc.perform(multipart("/api/import").file(file))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(Long.parseLong(result)).isEqualTo(10);
    }

    @Test
    public void importBadContentTypeTest() throws Exception {
        Resource fileResource = new ClassPathResource("/table/sample.xlsx");
        MockMultipartFile file = new MockMultipartFile("file",
                null,
                "application/my.content.type",
                fileResource.getInputStream());

        String result = mockMvc.perform(multipart("/api/import").file(file))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Bad file content type. Expected " +
                "'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' " +
                "but was 'application/my.content.type'.");
    }

    @Test
    public void importAsContentTest() throws Exception {
        Resource fileResource = new ClassPathResource("/table/sample.xlsx");
        MockMultipartFile file = new MockMultipartFile("file", fileResource.getFilename(),
                MediaType.APPLICATION_JSON_VALUE, fileResource.getInputStream());

        mockMvc.perform(post("/api/import")
                        .content(file.getBytes()))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void importNoFileTest() throws Exception {
        String result = mockMvc.perform(multipart("/api/import"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Required request part 'file' is not present");
    }

    @Test
    public void getImportJobDoneTest() throws Exception {
        String result = mockMvc.perform(get("/api/import/{id}", 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1,\"status\":\"DONE\",\"message\":\"import done\"}");
    }

    @Test
    public void getImportJobErrorTest() throws Exception {
        String result = mockMvc.perform(get("/api/import/{id}", 2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":2,\"status\":\"ERROR\",\"message\":\"import error\"}");
    }

    @Test
    public void getImportJobInProgressTest() throws Exception {
        String result = mockMvc.perform(get("/api/import/{id}", 3))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":3," +
                                                 "\"status\":\"IN PROGRESS\"," +
                                                 "\"message\":\"import in progress\"}");
    }

    @Test
    public void getImportJobBadIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/import/{id}", 10))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("IMPORT job with id '10' does not exist.");
    }

    @Test
    public void getImportJobExportIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/import/{id}", 4))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("IMPORT job with id '4' does not exist.");
    }

    @Test
    public void getExportJobDoneTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}", 4))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":4,\"status\":\"DONE\",\"message\":\"export done\"}");
    }

    @Test
    public void getExportJobErrorTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}", 5))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":5,\"status\":\"ERROR\",\"message\":\"export error\"}");
    }

    @Test
    public void getExportJobInProgressTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}", 6))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":6," +
                                                 "\"status\":\"IN PROGRESS\"," +
                                                 "\"message\":\"export in progress\"}");
    }

    @Test
    public void getExportJobBadIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}", 10))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("EXPORT job with id '10' does not exist.");
    }

    @Test
    public void getExportJobImportIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}", 1))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("EXPORT job with id '1' does not exist.");
    }

    @Test
    public void getFileDoesNotExistTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}/file", 4))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Failed to process export file. Please try exporting again.");
    }

    @Test
    public void getFileBadImportIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}/file", 1))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("EXPORT job with id '1' does not exist.");
    }

    @Test
    public void getFileBadIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}/file", 10))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("EXPORT job with id '10' does not exist.");
    }

    @Test
    public void getFileErrorTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}/file", 5))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Export job failed with message: 'export error'.");
    }

    @Test
    public void getFileInProgressTest() throws Exception {
        String result = mockMvc.perform(get("/api/export/{id}/file", 6))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Export job with id '6' is not finished yet.");
    }
}
