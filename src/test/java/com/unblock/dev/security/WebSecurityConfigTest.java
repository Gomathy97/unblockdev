package com.unblock.dev.security;

import com.unblock.dev.security.jwt.AuthEntryPointJwt;
import com.unblock.dev.security.jwt.AuthTokenFilter;
import com.unblock.dev.security.service.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WebSecurityConfigTest {
    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private AuthEntryPointJwt unauthorizedHandler;

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticationJwtTokenFilterBean() {
        AuthTokenFilter authTokenFilter = webSecurityConfig.authenticationJwtTokenFilter();
        assertNotNull(authTokenFilter);
    }

    @Test
    public void testAuthenticationProviderBean() {
        DaoAuthenticationProvider authProvider = webSecurityConfig.authenticationProvider();
        assertNotNull(authProvider);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

}
