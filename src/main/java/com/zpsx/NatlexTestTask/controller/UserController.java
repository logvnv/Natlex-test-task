package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.dto.UserPostRequestBody;
import com.zpsx.NatlexTestTask.domain.exception.RequestBodyValidationException;
import com.zpsx.NatlexTestTask.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="api/registration", consumes=MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserPostRequestBody user, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new RequestBodyValidationException(bindingResult.getAllErrors());
        userService.addUser(user);
    }
}
