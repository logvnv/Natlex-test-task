package com.zpsx.NatlexTestTask.controller;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/query/user_before_test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registrationTest() throws Exception {
        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test_user\",\"password\":\"12345678\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void registrationMinLengthTest() throws Exception {
        String result = mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Username must be at least 6 characters.");
        Assertions.assertThat(result).contains("Password must be at least 8 characters.");
    }

    @Test
    public void registrationMaxLengthTest() throws Exception {
        String result = mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + StringUtils.repeat("*", 33) + "\"," +
                                  "\"password\":\"" + StringUtils.repeat("*", 51) + "\"}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Username must be at most 32 characters.");
        Assertions.assertThat(result).contains("Password must be at most 50 characters.");
    }

    @Test
    public void registrationBlankTest() throws Exception {
        String result = mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uselessField\":1}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("Username must not be blank.");
        Assertions.assertThat(result).contains("Password must not be blank.");
    }

    @Test
    @Sql(statements = "DELETE FROM usr; INSERT INTO usr(id, password, username) VALUES" +
            "(1, '12345678', 'test01');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM usr;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registrationUserExistsTest() throws Exception {
        String result = mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test01\"," +
                                  "\"password\":\"12345678\"}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();

        Assertions.assertThat(result).contains("User 'test01' already exists.");
    }
}
