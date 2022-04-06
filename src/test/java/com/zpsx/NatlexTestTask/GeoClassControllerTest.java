package com.zpsx.NatlexTestTask;

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
@Sql(value = {"/query/geo_class_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/query/geo_class_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class GeoClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createGeoClassTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC12\"," +
                                  "\"name\":\"Geo Class 12\"}")
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":10," +
                                                 "\"code\":\"GC12\"," +
                                                 "\"name\":\"Geo Class 12\"}");
    }

    @Test
    public void createGeoClassFullDuplicateTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC11\"," +
                                  "\"name\":\"Geo Class 11\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result)
                .isEqualTo("Geological class with code 'GC11' and name 'Geo Class 11' already exists. It's id is '1'.");
    }

    @Test
    public void createGeoClassDuplicateCodeTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC11\"," +
                                  "\"name\":\"Geo Class 12\"}")
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":10," +
                                                 "\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 12\"}");
    }

    @Test
    public void createGeoClassDuplicateNameTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC11\"," +
                                  "\"name\":\"Geo Class 12\"}")
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":10," +
                                                 "\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 12\"}");
    }

    @Test
    public void createGeoClassPassIdTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GC12\"," +
                                  "\"name\":\"Geo Class 12\"," +
                                  "\"id\": 1}")
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":10," +
                                                 "\"code\":\"GC12\"," +
                                                 "\"name\":\"Geo Class 12\"}");
    }

    @Test
    public void createGeoClassBlankBodyTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"\"," +
                                  "\"name\":\"\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Geological class code must not be blank.");
        Assertions.assertThat(result).contains("Geological class name must not be blank.");
    }

    @Test
    public void createGeoClassTooLongBodyTest() throws Exception {
        String result = mockMvc.perform(post("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"" + StringUtils.repeat("*", 17) + "\"," +
                                  "\"name\":\"" + StringUtils.repeat("*", 256) + "\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Geological class code length must be below 16 characters.");
        Assertions.assertThat(result).contains("Geological class code length must be below 255 characters.");
    }

    @Test
    public void readGeoClassTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes/{id}", "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 11\"}");
    }

    @Test
    public void readGeoClassBadIdTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes/{id}", "2"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with id '2' does not exist.");
    }

    @Test
    public void readGeoClassByCodeAndNameTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes/by-code-and-name?code={code}&name={name}",
                        "GC11", "Geo Class 11"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 11\"}");
    }

    @Test
    public void readGeoClassByCodeAndNameBadTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes/by-code-and-name?code={code}&name={name}",
                        "GC10", "Geo Class 11"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code 'GC10'" +
                " and name 'Geo Class 11' does not exist.");
    }

    @Test
    public void readAllGeoClassesOneTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[{\"id\":1," +
                                                  "\"code\":\"GC11\"," +
                                                  "\"name\":\"Geo Class 11\"}]");
    }

    @Test
    @Sql(value = {"/query/geo_class_before_test.sql", "/query/geo_class_after_test.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/query/geo_class_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void readAllGeoClassesNoneTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[]");
    }

    @Test
    @Sql(value = {"/query/geo_class_before_test.sql"},
         statements = "insert into geo_class(id, code, name) values (2, 'GC12', 'Geo Class 12');",
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/query/geo_class_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void readAllGeoClassesMultipleTest() throws Exception {
        String result = mockMvc.perform(get("/api/geo-classes"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("[{\"id\":1," +
                                                  "\"code\":\"GC11\"," +
                                                  "\"name\":\"Geo Class 11\"}," +
                                                  "{\"id\":2," +
                                                  "\"code\":\"GC12\"," +
                                                  "\"name\":\"Geo Class 12\"}]");
    }

    @Test
    public void updateGeoClassTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                  "\"code\":\"GC111\"," +
                                  "\"name\":\"Geo Class 111\"}")
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"code\":\"GC111\"," +
                                                 "\"name\":\"Geo Class 111\"}");
    }

    @Test
    public void updateGeoClassBadIdTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2," +
                                  "\"code\":\"GC111\"," +
                                  "\"name\":\"Geo Class 111\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with id '2' does not exist.");
    }

    @Test
    @Sql(value = {"/query/geo_class_before_test.sql"},
            statements = "insert into geo_class(id, code, name) values (2, 'GC12', 'Geo Class 12');",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/query/geo_class_after_test.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateGeoClassDuplicateTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2," +
                                  "\"code\":\"GC11\"," +
                                  "\"name\":\"Geo Class 11\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with code 'GC11' and name 'Geo Class 11'" +
                " already exists. It's id is '1'.");
    }

    @Test
    public void updateGeoClassNoChangesTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1," +
                                  "\"code\":\"GC11\"," +
                                  "\"name\":\"Geo Class 11\"}")
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 11\"}");
    }

    @Test
    public void updateGeoClassBlankBodyTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"\"," +
                                  "\"name\":\"\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Geological class code must not be blank.");
        Assertions.assertThat(result).contains("Geological class name must not be blank.");
        Assertions.assertThat(result).contains("You must specify geological class id.");
    }

    @Test
    public void updateGeoClassTooLongBodyTest() throws Exception {
        String result = mockMvc.perform(put("/api/geo-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":0," +
                                  "\"code\":\"" + StringUtils.repeat("*", 17) + "\"," +
                                  "\"name\":\"" + StringUtils.repeat("*", 256) + "\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Geological class code length must be below 16 characters.");
        Assertions.assertThat(result).contains("Geological class code length must be below 255 characters.");
        Assertions.assertThat(result).contains("Geological class id must be a positive number.");
    }

    @Test
    public void deleteGeoClassTest() throws Exception {
        String result = mockMvc.perform(delete("/api/geo-classes/{id}", "1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(result).isEqualTo("{\"id\":1," +
                                                 "\"code\":\"GC11\"," +
                                                 "\"name\":\"Geo Class 11\"}");
    }

    @Test
    public void deleteGeoClassBadCodeTest() throws Exception {
        String result = mockMvc.perform(delete("/api/geo-classes/{id}", "2"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).isEqualTo("Geological class with id '2' does not exist.");
    }
}
