package com.unblock.dev.controller;

import com.unblock.dev.exception.AnswerNotFoundException;
import com.unblock.dev.exception.CannotAddAnswerToAnswerException;
import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.Answer;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.service.AnswerService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AnswerControllerTest {
    @Mock
    private AnswerService answerService;

    @InjectMocks
    private AnswerController answerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAnswerQuestion() throws UnprocessableEntityException, AnswerNotFoundException, QuestionNotFoundException, NoTagsFoundException {
        Long questionId = 1L;
        Answer answer = new Answer();

        when(answerService.answerQuestion(anyLong(), any(Answer.class))).thenReturn(answer);

        ResponseEntity responseEntity = answerController.answerQuestion(questionId, answer);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(answerService, times(1)).answerQuestion(anyLong(), any(Answer.class));
    }

    @Test
    public void testQuestionNotFoundException() throws UnprocessableEntityException, AnswerNotFoundException, QuestionNotFoundException, NoTagsFoundException {
        Long questionId = 1L;
        Answer answer = new Answer();

        when(answerService.answerQuestion(anyLong(), any(Answer.class)))
                .thenThrow(new QuestionNotFoundException("Question not found"));

        ResponseEntity responseEntity = answerController.answerQuestion(questionId, answer);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAnswerAnswer_ValidId() throws UnprocessableEntityException, AnswerNotFoundException, CannotAddAnswerToAnswerException, NoTagsFoundException {
        Long ansId = 1L;
        Answer answer = new Answer();
        when(answerService.answerAnswer(ansId, answer)).thenReturn(new Answer());

        ResponseEntity response = answerController.answerAnswer(ansId, answer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testAnswerAnswer_InvalidId() {
        Long ansId = null;
        Answer answer = new Answer();

        ResponseEntity response = answerController.answerAnswer(ansId, answer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAnswerAnswer_AnswerNotFoundException() throws UnprocessableEntityException, AnswerNotFoundException, CannotAddAnswerToAnswerException, NoTagsFoundException {
        Long ansId = 1L;
        Answer answer = new Answer();
        when(answerService.answerAnswer(ansId, answer)).thenThrow(new AnswerNotFoundException("Answer not found"));

        ResponseEntity response = answerController.answerAnswer(ansId, answer);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAnswerAnswer_UnprocessableEntityException() throws UnprocessableEntityException, AnswerNotFoundException, CannotAddAnswerToAnswerException, NoTagsFoundException {
        Long ansId = 1L;
        Answer answer = new Answer();
        when(answerService.answerAnswer(ansId, answer)).thenThrow(new UnprocessableEntityException("Unprocessable entity"));

        ResponseEntity response = answerController.answerAnswer(ansId, answer);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testAnswerAnswer_CannotAddAnswerToAnswerException() throws UnprocessableEntityException, AnswerNotFoundException, CannotAddAnswerToAnswerException, NoTagsFoundException {
        Long ansId = 1L;
        Answer answer = new Answer();
        when(answerService.answerAnswer(ansId, answer)).thenThrow(new CannotAddAnswerToAnswerException("Cannot add answer to answer"));

        ResponseEntity response = answerController.answerAnswer(ansId, answer);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testAnswerAnswer_InvalidIdentifierException() {
        Long ansId = null;
        Answer answer = new Answer();

        ResponseEntity response = answerController.answerAnswer(ansId, answer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAnswerAnswer_NoTagsFoundException() throws UnprocessableEntityException, AnswerNotFoundException, CannotAddAnswerToAnswerException, NoTagsFoundException {
        Long ansId = 1L;
        Answer answer = new Answer();
        when(answerService.answerAnswer(ansId, answer)).thenThrow(new NoTagsFoundException("No tags found"));

        ResponseEntity response = answerController.answerAnswer(ansId, answer);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAnswerQuestion_AnswerNotFoundException() throws UnprocessableEntityException, AnswerNotFoundException, QuestionNotFoundException, NoTagsFoundException {
        Long quesId = 1L;
        Answer answer = new Answer();
        when(answerService.answerQuestion(quesId, answer)).thenThrow(new AnswerNotFoundException("Answer not found"));

        ResponseEntity response = answerController.answerQuestion(quesId, answer);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAnswerQuestion_NoTagsFoundException() throws UnprocessableEntityException, AnswerNotFoundException, QuestionNotFoundException, NoTagsFoundException {
        Long quesId = 1L;
        Answer answer = new Answer();
        when(answerService.answerQuestion(quesId, answer)).thenThrow(new NoTagsFoundException("No tags found"));

        ResponseEntity response = answerController.answerQuestion(quesId, answer);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAnswerQuestion_UnprocessableEntityException() throws UnprocessableEntityException, AnswerNotFoundException, QuestionNotFoundException, NoTagsFoundException {
        Long quesId = 1L;
        Answer answer = new Answer();
        when(answerService.answerQuestion(quesId, answer)).thenThrow(new UnprocessableEntityException("Unprocessable entity"));

        ResponseEntity response = answerController.answerQuestion(quesId, answer);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testGetQuestionById_Success() throws AnswerNotFoundException {
        Long id = 1L;
        Answer answer = new Answer();
        when(answerService.getAnswerById(id)).thenReturn(answer);

        ResponseEntity response = answerController.getAnswerById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(answer, response.getBody());
    }

    @Test
    public void testGetQuestionById_AnswerNotFoundException() throws AnswerNotFoundException {
        Long id = 1L;
        when(answerService.getAnswerById(id)).thenThrow(new AnswerNotFoundException("Answer not found"));

        ResponseEntity response = answerController.getAnswerById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetQuestionById_InternalServerError() throws AnswerNotFoundException {
        Long id = 1L;
        when(answerService.getAnswerById(id)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity response = answerController.getAnswerById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_SuccessfulUpdate() throws AnswerNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(answerService.addTagsToAnswer(id, tagsRequest)).thenReturn(true);

        ResponseEntity response = answerController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_NoTagsFoundException() throws AnswerNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(answerService.addTagsToAnswer(id, tagsRequest)).thenThrow(new NoTagsFoundException("No tags found"));

        ResponseEntity response = answerController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_AnswerNotFoundException() throws AnswerNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(answerService.addTagsToAnswer(id, tagsRequest)).thenThrow(new AnswerNotFoundException("Answer not found"));

        ResponseEntity response = answerController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_RuntimeException() throws AnswerNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(answerService.addTagsToAnswer(id, tagsRequest)).thenReturn(false);

        ResponseEntity response = answerController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
