package com.unblock.dev.service;

import com.unblock.dev.model.User;
import com.unblock.dev.repository.UserRepository;
import com.unblock.dev.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceHelper {
    @Autowired
    UserRepository userRepository;
    public Optional<User> getLoggedInUser() {
        UserDetailsImpl currentLoggedInUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(currentLoggedInUser.getId());
    }

}
