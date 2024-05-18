package com.unblock.dev.service;

import com.unblock.dev.model.User;
import com.unblock.dev.repository.UserRepository;
import com.unblock.dev.security.service.UserDetailsImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceHelperTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ServiceHelper serviceHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLoggedInUser() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "password", null);
        User user = new User();
        user.setId(1L);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = serviceHelper.getLoggedInUser();

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetLoggedInUser_NotFound() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "username", "password", null);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = serviceHelper.getLoggedInUser();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(1L);
    }
}
