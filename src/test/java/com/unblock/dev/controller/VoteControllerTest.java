package com.unblock.dev.controller;

import com.unblock.dev.model.request.VoteRequest;
import com.unblock.dev.service.VoteService;
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
public class VoteControllerTest {
    @Mock
    private VoteService voteService;

    @InjectMocks
    private VoteController voteController;

    @Test
    public void testAddVote_SuccessfulUpdate() {
        VoteRequest voteRequest = new VoteRequest();
        when(voteService.addVote(voteRequest)).thenReturn(1);

        ResponseEntity response = voteController.addVote(voteRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddVote_RuntimeException() {
        VoteRequest voteRequest = new VoteRequest();
        when(voteService.addVote(voteRequest)).thenReturn(0);

        ResponseEntity response = voteController.addVote(voteRequest);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
