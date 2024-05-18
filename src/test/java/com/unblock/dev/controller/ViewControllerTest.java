package com.unblock.dev.controller;

import com.unblock.dev.model.request.ViewRequest;
import com.unblock.dev.service.ViewService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ViewControllerTest {
    @Mock
    private ViewService viewService;

    @InjectMocks
    private ViewController viewController;

    @Test
    public void testUpdateViews_SuccessfulUpdate() {
        ViewRequest viewRequest = new ViewRequest();
        when(viewService.addVote(viewRequest)).thenReturn(1);

        ResponseEntity response = viewController.updateViews(viewRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateViews_FailedUpdate() {
        ViewRequest viewRequest = new ViewRequest();
        when(viewService.addVote(viewRequest)).thenReturn(0);

        ResponseEntity response = viewController.updateViews(viewRequest);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testUpdateViews_RuntimeException() {
        ViewRequest viewRequest = new ViewRequest();
        when(viewService.addVote(viewRequest)).thenThrow(new RuntimeException("Unable to update vote"));

        ResponseEntity response = viewController.updateViews(viewRequest);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

}
