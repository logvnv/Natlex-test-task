package com.zpsx.NatlexTestTask.service;

import com.zpsx.NatlexTestTask.domain.User;
import com.zpsx.NatlexTestTask.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User \"%s\" not found.", username)));
    }

    public void addUser(User user) {
        if(userRepo.findByUsername(user.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("User \"%s\" already exists.", user.getUsername()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
