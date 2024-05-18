package com.unblock.dev.security.service;

import com.unblock.dev.model.User;
import com.unblock.dev.model.request.UserRequest;
import com.unblock.dev.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }

    @Test
    public void testLoadUserById_UserFound() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserById(id.toString());

        assertNotNull(userDetails);
    }

    @Test
    public void testLoadUserById_UserNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserById(id.toString()));
    }

    @Test
    public void testUpdateProfile_UserFound() {
        Long id = 1L;
        UserRequest userRequest = new UserRequest();
        userRequest.setFullName("John");
        userRequest.setDisplayName("Doe");
        User existingUser = new User();
        existingUser.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(existingUser);

        UserRequest updatedUser = userDetailsService.updateProfile(id, userRequest);

        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFullName());
        assertEquals("Doe", updatedUser.getDisplayName());
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateProfile_UserNotFound() {
        Long id = 1L;
        UserRequest userRequest = new UserRequest();
        userRequest.setFullName("John Doe");
        userRequest.setDisplayName("Doe");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.updateProfile(id, userRequest));
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testGetNullPropertyNames() {
        User user = new User();
        user.setFullName("John");
        user.setEmail("john@example.com");

        String[] nullProperties = userDetailsService.getNullPropertyNames(user);

        assertNotNull(nullProperties);
        assertEquals(13, nullProperties.length);
    }
}

