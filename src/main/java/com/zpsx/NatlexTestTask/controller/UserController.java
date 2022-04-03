package com.zpsx.NatlexTestTask.controller;

import com.zpsx.NatlexTestTask.domain.User;
import com.zpsx.NatlexTestTask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/registration", consumes=MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody User user){
        userService.addUser(user);
    }
}
