package com.zpsx.NatlexTestTask.controller;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
@Sql(value = {"/query/section_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/query/section_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SectionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createSectionNoGCTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 3\"," +
                                  "\"geoClasses\": []}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":3," +
                                                 "\"name\":\"Section 3\"," +
                                                 "\"geoClasses\":[]}");
    }

    @Test
    public void createSectionSingleGCTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 3\"," +
                                  "\"geoClasses\": [1]}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":3," +
                                                 "\"name\":\"Section 3\"," +
                                                 "\"geoClasses\":[{\"id\":1," +
                                                                  "\"code\":\"GC10\"," +
                                                                  "\"name\":\"Geo Class 10\"}]}");
    }

    @Test
    public void createSectionMultipleGCTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 3\"," +
                                "\"geoClasses\": [1,2]}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":3," +
                                                 "\"name\":\"Section 3\"," +
                                                 "\"geoClasses\":[{\"id\":1," +
                                                                  "\"code\":\"GC10\"," +
                                                                  "\"name\":\"Geo Class 10\"}," +
                                                                 "{\"id\":2," +
                                                                  "\"code\":\"GC11\"," +
                                                                  "\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void createSectionMissGCTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 3\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":3," +
                                                 "\"name\":\"Section 3\"," +
                                                 "\"geoClasses\":[]}");
    }

    @Test
    public void createSectionDuplicateNameTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 2\"," +
                                  "\"geoClasses\": [1]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with name 'Section 2' already exists, it's id is '2'.");
    }

    @Test
    public void createSectionBadGCTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 3\"," +
                                  "\"geoClasses\": [9]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with id '9' does not exist.");
    }

    @Test
    public void createSectionDuplicateGeoClassTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 11\"," +
                                  "\"geoClasses\": [1, 1]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Multiple occurrence of geological class with id '1'.");
    }

    @Test
    public void createSectionValTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"," +
                                "\"geoClasses\": [-1]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Section name must not be blank.");
        Assertions.assertThat(result).contains("Geological class ID must be a positive number.");
    }

    @Test
    public void createSectionTooLongNameTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + StringUtils.repeat("*", 256) + "\"}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Section name length must be below 255 characters.");
    }

    @Test
    public void readSectionTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/{id}", 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"name\":\"Section 1\"," +
                                                 "\"geoClasses\":[{\"id\":2," +
                                                                  "\"code\":\"GC11\"," +
                                                                  "\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void readSectionBadIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/{id}", 3))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with id '3' does not exit.");
    }

    @Test
    public void readSectionByNameTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/by-name?name={name}", "Section 1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"name\":\"Section 1\"," +
                                                 "\"geoClasses\":[{\"id\":2," +
                                                                  "\"code\":\"GC11\"," +
                                                                  "\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void readSectionByNameBadNameTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/by-name?name={name}", "Section 3"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with name 'Section 3' does not exit.");
    }

    @Test
    public void readSectionsByGeoCodeTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/by-code?code={code}", "GC11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[{\"id\":1," +
                                                  "\"name\":\"Section 1\"," +
                                                  "\"geoClasses\":[{" +
                                                      "\"id\":2," +
                                                      "\"code\":\"GC11\"," +
                                                      "\"name\":\"Geo Class 11\"}]}]");
    }

    @Test
    @Sql(value = {"/query/section_before_test.sql"},
            statements = "insert into section_geo_classes(section_id, geo_classes_id) values (2, 2);",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/query/section_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void readSectionsByGeoCodeMultipleTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/by-code?code={code}", "GC11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[{\"id\":1," +
                                                  "\"name\":\"Section 1\"," +
                                                  "\"geoClasses\":" +
                                                      "[{\"id\":2," +
                                                        "\"code\":\"GC11\"," +
                                                        "\"name\":\"Geo Class 11\"}]}," +
                                                 "{\"id\":2," +
                                                  "\"name\":\"Section 2\"," +
                                                  "\"geoClasses\":" +
                                                      "[{\"id\":1," +
                                                        "\"code\":\"GC10\"," +
                                                        "\"name\":\"Geo Class 10\"}," +
                                                       "{\"id\":2," +
                                                        "\"code\":\"GC11\"," +
                                                        "\"name\":\"Geo Class 11\"}]}]");
    }

    @Test
    public void readSectionsBadGeoCodeTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/by-code?code={code}", "GC111"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[]");
    }

    @Test
    public void readAllSectionsTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[{\"id\":1," +
                                                  "\"name\":\"Section 1\"," +
                                                  "\"geoClasses\":[{" +
                                                      "\"id\":2," +
                                                      "\"code\":\"GC11\"," +
                                                      "\"name\":\"Geo Class 11\"}]}," +
                                                 "{\"id\":2," +
                                                  "\"name\":\"Section 2\"," +
                                                  "\"geoClasses\":[{" +
                                                      "\"id\":1," +
                                                      "\"code\":\"GC10\"," +
                                                      "\"name\":\"Geo Class 10\"}]}]");
    }

    @Test
    public void updateSectionTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                  "\"name\":\"Section 11\"," +
                                  "\"geoClasses\":[1]}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"name\":\"Section 11\"," +
                                                 "\"geoClasses\":[{" +
                                                     "\"id\":1," +
                                                     "\"code\":\"GC10\"," +
                                                     "\"name\":\"Geo Class 10\"}]}");
    }

    @Test
    public void updateSectionDuplicateNameTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2," +
                                  "\"name\":\"Section 1\"," +
                                  "\"geoClasses\":[1]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with name 'Section 1' already exists, it's id is '1'.");
    }

    @Test
    public void updateSectionEmptyGCTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                "\"name\":\"Section 11\"," +
                                "\"geoClasses\":[]}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"name\":\"Section 11\"," +
                                                 "\"geoClasses\":[]}");
    }

    @Test
    public void updateSectionMissGCTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                "\"name\":\"Section 11\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"name\":\"Section 11\"," +
                                                 "\"geoClasses\":[]}");
    }

    @Test
    public void updateSectionBadIdTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3," +
                                  "\"name\":\"Section 1\"," +
                                  "\"geoClasses\":[1]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with id '3' does not exit.");
    }

    @Test
    public void updateSectionBadGCTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                  "\"name\":\"Section 1\"," +
                                  "\"geoClasses\": [3]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with id '3' does not exist.");
    }

    @Test
    public void updateSectionValidationTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":-1," +
                                  "\"name\":\"Section 1\"," +
                                  "\"geoClasses\": [3]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section id must be a positive number.");
    }

    @Test
    public void updateSectionDuplicateGeoClassTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                  "\"name\":\"Section 11\"," +
                                  "\"geoClasses\": [1, 1]}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Multiple occurrence of geological class with id '1'.");
    }

    @Test
    public void deleteSectionTest() throws Exception {
        String result = mockMvc.perform(delete("/api/sections/{id}", 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"name\":\"Section 1\"," +
                                                 "\"geoClasses\":[{" +
                                                     "\"id\":2," +
                                                     "\"code\":\"GC11\"," +
                                                     "\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void deleteSectionBadIdTest() throws Exception {
        String result = mockMvc.perform(delete("/api/sections/{id}", 3))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with id '3' does not exit.");
    }
}
