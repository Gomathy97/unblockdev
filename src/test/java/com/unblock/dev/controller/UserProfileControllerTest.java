package com.unblock.dev.controller;

import com.unblock.dev.model.request.UserRequest;
import com.unblock.dev.security.service.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserProfileControllerTest {
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private UserProfileController userProfileController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateProfile_Success() {
        Long userId = 1L;
        UserRequest updatedUser = new UserRequest();
        updatedUser.setDisplayName("displayname");
        updatedUser.setFullName("updatedName");
        when(userDetailsService.updateProfile(userId, updatedUser)).thenReturn(updatedUser);

        ResponseEntity response = userProfileController.updateProfile(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserRequest responseBody = (UserRequest) response.getBody();
        assertEquals("displayname", responseBody.getDisplayName());
        assertEquals("updatedName", responseBody.getFullName());

        verify(userDetailsService, times(1)).updateProfile(userId, updatedUser);
    }

    @Test
    public void testUpdateProfile_Error() {
        Long userId = 1L;
        UserRequest updatedUser = new UserRequest();
        updatedUser.setDisplayName("displayname");
        when(userDetailsService.updateProfile(userId, updatedUser)).thenReturn(null);

        ResponseEntity response = userProfileController.updateProfile(userId, updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userDetailsService, times(1)).updateProfile(userId, updatedUser);
    }
}
