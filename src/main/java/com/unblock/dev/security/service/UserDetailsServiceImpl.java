package com.unblock.dev.security.service;

import com.unblock.dev.model.User;
import com.unblock.dev.model.request.UserRequest;
import com.unblock.dev.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email ));

        return UserDetailsImpl.build(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id ));

        return UserDetailsImpl.build(user);
    }

    public UserRequest updateProfile(@NonNull Long id, @NonNull UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id ));

        BeanUtils.copyProperties(userRequest, user, getNullPropertyNames(userRequest));
        user.setTimeUpdated(new Date().getTime());
        User userResponse = userRepository.save(user);
        BeanUtils.copyProperties(userResponse, userRequest, getNullPropertyNames(userRequest));

        return userRequest;
    }

    String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getName().equals("timeCreated") || pd.getName().equals("timeUpdated")) continue;
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
