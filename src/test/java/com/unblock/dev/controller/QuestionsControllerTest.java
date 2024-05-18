package com.unblock.dev.controller;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.model.response.ResponseDetails;
import com.unblock.dev.service.QuestionService;
import com.unblock.dev.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionsControllerTest {
    private QuestionService questionService = mock(QuestionService.class);

    @InjectMocks
    private QuestionsController questionController;

    @Mock
    private TagService tagService;

    @Test
    public void testAskQuestion_SuccessfulCreation() throws UnprocessableEntityException, QuestionNotFoundException, NoTagsFoundException {
        Question question = new Question();
        when(questionService.askQuestion(question)).thenReturn(new Question());

        ResponseEntity response = questionController.askQuestion(question);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testAskQuestion_UnprocessableEntityException() throws UnprocessableEntityException, QuestionNotFoundException, NoTagsFoundException {
        Question question = new Question();
        when(questionService.askQuestion(question)).thenThrow(new UnprocessableEntityException("Unprocessable entity"));

        ResponseEntity response = questionController.askQuestion(question);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testAskQuestion_NoTagsFoundException() throws UnprocessableEntityException, QuestionNotFoundException, NoTagsFoundException {
        Question question = new Question();
        when(questionService.askQuestion(question)).thenThrow(new NoTagsFoundException("No tags found"));

        ResponseEntity response = questionController.askQuestion(question);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAskQuestion_InternalServerError() throws UnprocessableEntityException, QuestionNotFoundException, NoTagsFoundException {
        Question question = new Question();
        when(questionService.askQuestion(question)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity response = questionController.askQuestion(question);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetQuestionById_Successful() throws QuestionNotFoundException {
        Long id = 1L;
        when(questionService.getQuestionById(id)).thenReturn(new Question());

        ResponseEntity response = questionController.getQuestionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetQuestionById_QuestionNotFoundException() throws QuestionNotFoundException {
        Long id = 1L;
        when(questionService.getQuestionById(id)).thenThrow(new QuestionNotFoundException("Question not found"));

        ResponseEntity response = questionController.getQuestionById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetQuestionById_InternalServerError() throws QuestionNotFoundException {
        Long id = 1L;
        when(questionService.getQuestionById(id)).thenThrow(new RuntimeException("Internal Server Error"));

        ResponseEntity response = questionController.getQuestionById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAskQuestion_UnexpectedException() throws UnprocessableEntityException, QuestionNotFoundException, NoTagsFoundException {
        Question question = new Question();
        when(questionService.askQuestion(question)).thenThrow(new NullPointerException("Unexpected exception"));

        ResponseEntity response = questionController.askQuestion(question);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetQuestionById_UnexpectedException() throws QuestionNotFoundException {
        Long id = 1L;
        when(questionService.getQuestionById(id)).thenThrow(new NullPointerException("Unexpected exception"));

        ResponseEntity response = questionController.getQuestionById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_SuccessfulUpdate() throws QuestionNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(questionService.addTagsToQuestion(id, tagsRequest)).thenReturn(true);

        ResponseEntity response = questionController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_NoTagsFoundException() throws QuestionNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(questionService.addTagsToQuestion(id, tagsRequest)).thenThrow(new NoTagsFoundException("No tags found"));

        ResponseEntity response = questionController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_QuestionNotFoundException() throws QuestionNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(questionService.addTagsToQuestion(id, tagsRequest)).thenThrow(new QuestionNotFoundException("Question not found"));

        ResponseEntity response = questionController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddTagsToQuestion_RuntimeException() throws QuestionNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest tagsRequest = new TagsRequest();
        when(questionService.addTagsToQuestion(id, tagsRequest)).thenReturn(false);

        ResponseEntity response = questionController.addTagsToQuestion(id, tagsRequest);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testGetQuestionByText_Search_Success() throws QuestionNotFoundException {
        String search = "test";
        int page = 0;
        int perPage = 10;

        when(questionService.getQuestionByText(search, page, perPage)).thenReturn(Arrays.asList(new Question()));

        ResponseEntity response = questionController.getQuestionByText(search, null, page, perPage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(questionService, times(1)).getQuestionByText(search, page, perPage);
    }

    @Test
    public void testGetQuestionByText_Tag_Success() throws NoTagsFoundException {
        String tag = "java";
        int page = 0;
        int perPage = 10;

        when(tagService.getQuestionsByTags(tag, page, perPage)).thenReturn(Arrays.asList(new Question()));

        ResponseEntity response = questionController.getQuestionByText(null, tag, page, perPage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tagService, times(1)).getQuestionsByTags(tag, page, perPage);
    }

    @Test
    public void testGetQuestionByText_QuestionNotFoundException() throws QuestionNotFoundException {
        String search = "test";
        int page = 0;
        int perPage = 10;

        when(questionService.getQuestionByText(search, page, perPage)).thenThrow(new QuestionNotFoundException("Question not found"));

        ResponseEntity response = questionController.getQuestionByText(search, null, page, perPage);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(questionService, times(1)).getQuestionByText(search, page, perPage);
    }

    @Test
    public void testGetQuestionByText_NoTagsFoundException() throws NoTagsFoundException {
        String tag = "java";
        int page = 0;
        int perPage = 10;

        when(tagService.getQuestionsByTags(tag, page, perPage)).thenThrow(new NoTagsFoundException("No tags found"));

        ResponseEntity response = questionController.getQuestionByText(null, tag, page, perPage);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tagService, times(1)).getQuestionsByTags(tag, page, perPage);
    }

    @Test
    public void testGetQuestionsOfAUser_Success() throws QuestionNotFoundException {
        Long userId = 1L;
        int page = 0;
        int perPage = 10;

        when(questionService.getQuestionsOfAUser(userId, page, perPage)).thenReturn(Arrays.asList(new Question()));

        ResponseEntity response = questionController.getQuestionsOfAUser(userId, page, perPage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(questionService, times(1)).getQuestionsOfAUser(userId, page, perPage);
    }

    @Test
    public void testGetQuestionsOfAUser_UsernameNotFoundException() throws QuestionNotFoundException {
        Long userId = 1L;
        int page = 0;
        int perPage = 10;

        when(questionService.getQuestionsOfAUser(userId, page, perPage)).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity response = questionController.getQuestionsOfAUser(userId, page, perPage);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(questionService, times(1)).getQuestionsOfAUser(userId, page, perPage);
    }

    @Test
    public void testGetQuestionsOfAUser_QuestionNotFoundException() throws QuestionNotFoundException {
        Long userId = 1L;
        int page = 0;
        int perPage = 10;

        when(questionService.getQuestionsOfAUser(userId, page, perPage)).thenThrow(new QuestionNotFoundException("Question not found"));

        ResponseEntity response = questionController.getQuestionsOfAUser(userId, page, perPage);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(questionService, times(1)).getQuestionsOfAUser(userId, page, perPage);
    }

    @Test
    public void testGetQuestionsOfAUser_InternalServerError() throws QuestionNotFoundException {
        Long userId = 1L;
        int page = 0;
        int perPage = 10;

        when(questionService.getQuestionsOfAUser(userId, page, perPage)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity response = questionController.getQuestionsOfAUser(userId, page, perPage);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testResolveQuestion_Success() throws QuestionNotFoundException {
        Long questionId = 1L;
        when(questionService.markAsResolved(questionId)).thenReturn(1);

        ResponseEntity response = questionController.resolveQuestion(questionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(questionService).markAsResolved(questionId);
    }

    @Test
    public void testResolveQuestion_NotFound() throws QuestionNotFoundException {
        Long questionId = 1L;
        when(questionService.markAsResolved(questionId)).thenThrow(new QuestionNotFoundException("Question not found"));

        ResponseEntity response = questionController.resolveQuestion(questionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDetails);
        assertEquals("Question not found", ((ResponseDetails) response.getBody()).getMessage());
        assertEquals("/id", ((ResponseDetails) response.getBody()).getDetails().getField());
        assertEquals(questionId.toString(), ((ResponseDetails) response.getBody()).getDetails().getValue());
    }

    @Test
    public void testResolveQuestion_Unauthorized() throws QuestionNotFoundException {
        // Arrange
        Long questionId = 1L;
        when(questionService.markAsResolved(questionId)).thenThrow(new AuthenticationServiceException("Not Authorized"));

        // Act
        ResponseEntity response = questionController.resolveQuestion(questionId);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDetails);
        assertEquals("Not Authorized", ((ResponseDetails) response.getBody()).getMessage());
        assertEquals("Authorization", ((ResponseDetails) response.getBody()).getDetails().getField());
        assertNull(((ResponseDetails) response.getBody()).getDetails().getValue());
    }

    @Test
    public void testResolveQuestion_UnprocessableEntity() throws QuestionNotFoundException {
        // Arrange
        Long questionId = 1L;
        when(questionService.markAsResolved(questionId)).thenThrow(new RuntimeException("Tag update failed"));

        // Act
        ResponseEntity response = questionController.resolveQuestion(questionId);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody() instanceof ResponseDetails);
        assertEquals("Tag update failed", ((ResponseDetails) response.getBody()).getMessage());
        assertEquals("/id", ((ResponseDetails) response.getBody()).getDetails().getField());
        assertEquals(questionId.toString(), ((ResponseDetails) response.getBody()).getDetails().getValue());
    }
}
