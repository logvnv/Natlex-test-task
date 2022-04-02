package com.zpsx.NatlexTestTask;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/query/geo_class_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/query/geo_class_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GeoClassControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createGeoClassTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC12\"," +
                                "\"name\":\"Geo Class 12\"}")
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"code\":\"GC12\"," +
                                                 "\"name\":\"Geo Class 12\"}");
    }

    @Test
    public void createGeoClassDuplicateTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC11\"," +
                                "\"name\":\"Geo Class 11\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code \"GC11\" already exists.");
    }

    @Test
    public void readGeoClassTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes/{code}", "GC11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 11\"}");
    }

    @Test
    public void readGeoClassBadCodeTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes/{code}", "GC12"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code \"GC12\" does not exist.");
    }

    @Test
    public void updateGeoClassTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC11\"," +
                                  "\"name\":\"Geo Class 111\"}")
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 111\"}");
    }

    @Test
    public void updateGeoClassBadCodeTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC12\"," +
                                "\"name\":\"Geo Class 12\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code \"GC12\" does not exist.");
    }

    @Test
    public void deleteGeoClassTest() throws Exception {
        String result = mockMvc.perform(delete("/api/geo-classes/{code}", "GC11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"code\":\"GC11\"," +
                "\"name\":\"Geo Class 11\"}");
    }

    @Test
    public void deleteGeoClassBadCodeTest() throws Exception {
        String result = mockMvc.perform(delete("/api/geo-classes/{code}", "GC12"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code \"GC12\" does not exist.");
    }

    @Test
    public void readAllGeoClassTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[{\"code\":\"GC11\"," +
                "\"name\":\"Geo Class 11\"}]");
    }
}
