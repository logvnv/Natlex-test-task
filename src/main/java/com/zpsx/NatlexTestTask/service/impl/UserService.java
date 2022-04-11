package com.zpsx.NatlexTestTask.service.impl;

import com.zpsx.NatlexTestTask.domain.User;
import com.zpsx.NatlexTestTask.domain.dto.UserPostRequestBody;
import com.zpsx.NatlexTestTask.repository.UserRepo;
import com.zpsx.NatlexTestTask.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService, IUserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User '%s' not found.", username)));
    }

    @Override
    public void addUser(UserPostRequestBody userPostRequestBody) {
        if(userRepo.findByUsername(userPostRequestBody.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User '%s' already exists.", userPostRequestBody.getUsername()));

        userRepo.save(new User(userPostRequestBody.getUsername(),
                passwordEncoder.encode(userPostRequestBody.getPassword())));
    }
}
