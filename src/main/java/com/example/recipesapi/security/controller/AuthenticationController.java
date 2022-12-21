package com.example.recipesapi.security.controller;

import com.example.recipesapi.security.model.AuthenticationRequest;
import com.example.recipesapi.security.model.entity.User;
import com.example.recipesapi.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    public final UserService userService;

    public AuthenticationController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(userService.registerUser(authenticationRequest), HttpStatus.OK);
    }

}