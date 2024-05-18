package com.unblock.dev.controller;

import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.service.QuestionScoreService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionScoreControllerTest {
    @Mock
    private QuestionScoreService questionScoreService;

    @Mock
    private QuestionsController questionController;

    @InjectMocks
    private QuestionScoreController questionScoreController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetQuestionForQuestion_SuccessfulUpdate() throws QuestionNotFoundException {
        long[] questions = {1, 2, 3};
        when(questionScoreService.updateScores(questions)).thenReturn(true);

        ResponseEntity response = questionScoreController.getQuestionForQuestion(questions);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetQuestionForQuestion_QuestionNotFoundException() throws QuestionNotFoundException {
        long[] questions = {1, 2, 3};
        when(questionScoreService.updateScores(questions)).thenThrow(new QuestionNotFoundException("Question not found"));

        ResponseEntity response = questionScoreController.getQuestionForQuestion(questions);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testGetQuestionsByFilter_Successful() {
        String filter = "example";
        int page = 1;
        int perPage = 10;
        when(questionScoreService.getTopQuestions(filter, page, perPage)).thenReturn(new ArrayList<>());

        ResponseEntity response = questionScoreController.getQuestionsByFilter(filter, page, perPage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetQuestionsByFilter_UnprocessableEntity() {
        String filter = "invalid_filter";
        int page = 1;
        int perPage = 10;
        when(questionScoreService.getTopQuestions(filter, page, perPage)).thenThrow(new RuntimeException("Invalid filter"));

        ResponseEntity response = questionScoreController.getQuestionsByFilter(filter, page, perPage);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
