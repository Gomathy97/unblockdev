package com.unblock.dev.controller;

import com.unblock.dev.model.response.JwtResponse;
import com.unblock.dev.model.response.Response;
import com.unblock.dev.model.request.Auth;
import com.unblock.dev.model.User;
import com.unblock.dev.repository.UserRepository;
import com.unblock.dev.security.service.UserDetailsImpl;
import com.unblock.dev.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping(value = "/signup", produces  = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity signup(@RequestBody Auth authRequest) {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new Response("Error: Username is already taken!"));
        }

        User user = new User(authRequest.getEmail(), encoder.encode(authRequest.getPassword()));
        user.setTimeCreated(new Date().getTime());
        user.setTimeUpdated(new Date().getTime());
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    ResponseEntity signin(@RequestBody Auth user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail()));
    }
}
