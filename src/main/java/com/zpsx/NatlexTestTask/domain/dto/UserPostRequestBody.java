package com.zpsx.NatlexTestTask.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserPostRequestBody {
    @NotBlank(message = "Username must not be blank.")
    @Size(min = 6, message = "Username must be at least 6 characters.")
    @Size(max = 32, message = "Username must be at most 32 characters.")
    private String username;

    @NotBlank(message = "Password must not be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    @Size(max = 50, message = "Password must be at most 50 characters.")
    private String password;
}
