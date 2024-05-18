package com.unblock.dev.security.jwt;

import com.unblock.dev.security.service.UserDetailsServiceImpl;
import com.unblock.dev.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthTokenFilterTest {
    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoFilterInternal_ValidJwtToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(jwtUtils.validateJwtToken(anyString(), any())).thenReturn(true);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(request, atLeastOnce()).getHeader("Authorization");
        verify(filterChain, atLeastOnce()).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }

    @Test
    public void testDoFilterInternal_InvalidJwtToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(request, atLeastOnce()).getHeader("Authorization");
    }
}
