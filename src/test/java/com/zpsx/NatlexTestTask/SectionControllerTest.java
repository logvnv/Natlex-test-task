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
@Sql(value = {"/query/section_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/query/section_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SectionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createSectionTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 2\"," +
                                  "\"geoCodes\": [\"GC11\"]}")
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":3," +
                                                 "\"name\":\"Section 2\"," +
                                                 "\"geoClasses\":[{\"code\":\"GC11\",\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void createSectionBadCodeTest() throws Exception {
        String result = mockMvc.perform(post("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Section 2\"," +
                                "\"geoCodes\": [\"GC12\"]}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code \"GC12\" does not exist.");
    }

    @Test
    public void readSectionTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/{id}", 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"name\":\"Section 1\"," +
                                                 "\"geoClasses\":[{\"code\":\"GC11\",\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void readSectionBadIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/{id}", 3))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with id \"3\" does not exit.");
    }

    @Test
    public void updateSectionTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                  "\"name\":\"Section 11\"," +
                                  "\"geoCodes\":[\"GC11\"]}")
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                "\"name\":\"Section 11\"," +
                "\"geoClasses\":[{\"code\":\"GC11\",\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void updateSectionBadIdTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":3," +
                                  "\"name\":\"Section 1\"," +
                                  "\"geoCodes\":[\"GC11\"]}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with id \"3\" does not exit.");
    }

    @Test
    public void updateSectionBadCodeTest() throws Exception {
        String result = mockMvc.perform(put("/api/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                  "\"name\":\"Section 1\"," +
                                  "\"geoCodes\": [\"GC12\"]}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code \"GC12\" does not exist.");
    }

    @Test
    public void deleteSectionTest() throws Exception {
        String result = mockMvc.perform(delete("/api/sections/{id}", 1))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                "\"name\":\"Section 1\"," +
                "\"geoClasses\":[{\"code\":\"GC11\",\"name\":\"Geo Class 11\"}]}");
    }

    @Test
    public void deleteSectionBadIdTest() throws Exception {
        String result = mockMvc.perform(delete("/api/sections/{id}", 3))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Section with id \"3\" does not exit.");
    }

    @Test
    public void readAllSectionsTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertThat(result).isEqualTo("[{\"id\":1," +
                                                  "\"name\":\"Section 1\"," +
                                                  "\"geoClasses\":" +
                                                      "[{\"code\":\"GC11\",\"name\":\"Geo Class 11\"}]}," +
                                                 "{\"id\":2," +
                                                  "\"name\":\"Section 2\"," +
                                                  "\"geoClasses\":" +
                                                      "[{\"code\":\"GC10\",\"name\":\"Geo Class 10\"}]}]");
    }

    @Test
    public void readSectionsByGeoCodeTest() throws Exception {
        String result = mockMvc.perform(get("/api/sections/by-code?code={code}", "GC11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertThat(result).isEqualTo("[{\"id\":1," +
                "\"name\":\"Section 1\"," +
                "\"geoClasses\":" +
                "[{\"code\":\"GC11\",\"name\":\"Geo Class 11\"}]}]");
    }
}
