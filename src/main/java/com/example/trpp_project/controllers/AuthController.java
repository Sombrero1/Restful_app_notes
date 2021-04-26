package com.example.trpp_project.controllers;

import com.example.trpp_project.dto.AuthenticationRequestDto;
import com.example.trpp_project.models.User;
import com.example.trpp_project.repo.UserRepository;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @PostMapping("/register")
    public String sign_up(@Validated @RequestBody AuthenticationRequestDto authenticationRequestDto){
        if (userRepository.findByUsername(authenticationRequestDto.getUsername())!= null){
            throw new ResponseStatusException(//ВЫПОЛНИТЬ ПРОВЕРКУ
                    HttpStatus.UNAUTHORIZED, "the username is already in use"
            );

        }
        User user = new User();
        user.setUsername(authenticationRequestDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(authenticationRequestDto.getPassword()));
        userRepository.save(user);


        return "OK";
    }
}
