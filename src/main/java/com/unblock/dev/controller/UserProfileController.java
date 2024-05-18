package com.unblock.dev.controller;

import com.unblock.dev.model.User;
import com.unblock.dev.model.request.UserRequest;
import com.unblock.dev.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserProfileController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PutMapping(value = "update_profile/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity updateProfile(@PathVariable("id") Long id, @RequestBody UserRequest user) {
        UserRequest response = userDetailsService.updateProfile(id, user);
        if (response == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
