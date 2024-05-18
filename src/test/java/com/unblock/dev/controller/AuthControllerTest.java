package com.unblock.dev.controller;

import com.unblock.dev.model.request.Auth;
import com.unblock.dev.model.response.JwtResponse;
import com.unblock.dev.model.response.Response;
import com.unblock.dev.model.User;
import com.unblock.dev.repository.UserRepository;
import com.unblock.dev.security.service.UserDetailsImpl;
import com.unblock.dev.security.utils.JwtUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup_Success() {
        Auth authRequest = new Auth("test@example.com", "password");
        User newUser = new User("test@example.com", "encodedPassword");
        when(userRepository.existsByEmail(authRequest.getEmail())).thenReturn(false);
        when(encoder.encode(authRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        ResponseEntity response = authController.signup(authRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(userRepository, times(1)).existsByEmail(authRequest.getEmail());
        verify(encoder, times(1)).encode(authRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignup_EmailAlreadyTaken() {
        Auth authRequest = new Auth("test@example.com", "password");
        when(userRepository.existsByEmail(authRequest.getEmail())).thenReturn(true);

        ResponseEntity response = authController.signup(authRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Response responseBody = (Response) response.getBody();
        assertEquals("Error: Username is already taken!", responseBody.getMessage());

        verify(userRepository, times(1)).existsByEmail(authRequest.getEmail());
        verifyNoMoreInteractions(encoder, userRepository);
    }

    @Test
    public void testSignin_Success() {
        Auth authRequest = new Auth("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userDetails.getEmail()).thenReturn("test@example.com");

        ResponseEntity response = authController.signin(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse responseBody = (JwtResponse) response.getBody();
        assertEquals("jwtToken", responseBody.getToken());
        assertEquals("test@example.com", responseBody.getUsername());
        assertEquals("test@example.com", responseBody.getEmail());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
        verify(authentication, times(1)).getPrincipal();
        verify(userDetails, times(1)).getUsername();
        verify(userDetails, times(1)).getEmail();
    }
}
