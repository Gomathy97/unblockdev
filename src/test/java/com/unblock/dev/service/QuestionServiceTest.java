package com.unblock.dev.service;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.Answer;
import com.unblock.dev.model.AnswerEntity;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.QuestionEntity;
import com.unblock.dev.model.QuestionTagEntity;
import com.unblock.dev.model.Tags;
import com.unblock.dev.model.User;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.QuestionTagEntityRepository;
import com.unblock.dev.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private QuestionTagEntityRepository questionTagEntityRepository;

    @Mock
    private ServiceHelper serviceHelper;

    @Mock
    private TagService tagService;

    @InjectMocks
    private QuestionService questionService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAskQuestion_Successful() throws Exception {
        Question question = new Question();
        question.setId(1l);
        question.setTags(new String[]{"tag1, tag2"});

        User user = new User();
        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(user));
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
        Tags tag1 = new Tags();
        tag1.setTag("tag1");
        Tags tag2 = new Tags();
        tag2.setTag("tag2");
        when(tagRepository.findAllByTag(any())).thenReturn(List.of(tag1, tag2));

        Question result = questionService.askQuestion(question);

        assertNotNull(result);
        verify(questionRepository, times(1)).save(question);
        verify(tagRepository, times(1)).findAllByTag(any());
        verify(questionTagEntityRepository, times(2)).save(any(QuestionTagEntity.class));
    }

    @Test
    public void testAskQuestion_UnprocessableEntityException() {
        Question question = new Question();
        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(UnprocessableEntityException.class, () -> {
            questionService.askQuestion(question);
        });
    }

    @Test
    public void testAskQuestion_NoTagsFoundException() {
        Question question = new Question();
        question.setTags(new String[]{"tag1, tag2"});

        User user = new User();
        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(user));
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(tagRepository.findAllByTag(any())).thenReturn(Collections.emptyList());

        assertThrows(NoTagsFoundException.class, () -> {
            questionService.askQuestion(question);
        });
    }

    @Test
    public void testGetQuestionById_Successful() throws Exception {
        Long id = 1L;
        Question question = new Question();
        when(questionRepository.findById(id)).thenReturn(Optional.of(question));

        Question result = questionService.getQuestionById(id);

        assertNotNull(result);
        assertEquals(question, result);
    }

    @Test
    public void testGetQuestionById_QuestionNotFoundException() {
        Long id = 1L;
        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> {
            questionService.getQuestionById(id);
        });
    }

    @Test
    public void testGetQuestionByText_Successful() throws Exception {
        String text = "test";
        int page = 0;
        int per_page = 10;
        Pageable pageable = PageRequest.of(page, per_page, Sort.by("timeCreated").descending());
        List<Question> questions = List.of(new Question());

        when(questionRepository.findQuestionByText(text, pageable)).thenReturn(questions);

        List<Question> result = questionService.getQuestionByText(text, page, per_page);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(questions, result);
    }

    @Test
    public void testGetQuestionByText_QuestionNotFoundException() {
        String text = "test";
        int page = 0;
        int per_page = 10;
        Pageable pageable = PageRequest.of(page, per_page, Sort.by("timeCreated").descending());

        when(questionRepository.findQuestionByText(text, pageable)).thenReturn(Collections.emptyList());

        assertThrows(QuestionNotFoundException.class, () -> {
            questionService.getQuestionByText(text, page, per_page);
        });
    }

    @Test
    public void testGetQuestionsOfAUser_Successful() throws Exception {
        Long id = 1L;
        int page = 0;
        int per_page = 10;
        Pageable pageable = PageRequest.of(page, per_page, Sort.by("timeCreated").descending());
        List<Question> questions = List.of(new Question());

        when(questionRepository.findByUserId(id, pageable)).thenReturn(questions);

        List<Question> result = questionService.getQuestionsOfAUser(id, page, per_page);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(questions, result);
    }

    @Test
    public void testGetQuestionsOfAUser_QuestionNotFoundException() {
        Long id = 1L;
        int page = 0;
        int per_page = 10;
        Pageable pageable = PageRequest.of(page, per_page, Sort.by("timeCreated").descending());

        when(questionRepository.findByUserId(id, pageable)).thenReturn(Collections.emptyList());

        assertThrows(QuestionNotFoundException.class, () -> {
            questionService.getQuestionsOfAUser(id, page, per_page);
        });
    }

    @Test
    public void testAddTagsToQuestion_Successful() throws Exception {
        Long id = 1L;
        TagsRequest request = new TagsRequest();
        request.setTags(new String[]{"tag1, tag2"});

        Tags tag1 = new Tags();
        tag1.setTag("tag1");
        Tags tag2 = new Tags();
        tag2.setTag("tag2");

        List<Tags> tags = List.of(tag1, tag2);
        when(tagRepository.findAllByTag(any())).thenReturn(tags);
        when(questionRepository.findById(id)).thenReturn(Optional.of(new Question()));

        Boolean result = questionService.addTagsToQuestion(id, request);

        assertTrue(result);
        verify(questionTagEntityRepository, times(2)).save(any(QuestionTagEntity.class));
    }

    @Test
    public void testAddTagsToQuestion_NoTagsFoundException() {
        Long id = 1L;
        TagsRequest request = new TagsRequest();
        request.setTags(new String[]{"tag1, tag2"});

        when(tagRepository.findAllByTag(any())).thenReturn(Collections.emptyList());

        assertThrows(NoTagsFoundException.class, () -> {
            questionService.addTagsToQuestion(id, request);
        });
    }

    @Test
    public void testAddTagsToQuestion_QuestionNotFoundException() {
        Long id = 1L;
        TagsRequest request = new TagsRequest();
        request.setTags(new String[]{"tag1, tag2"});

        Tags tag1 = new Tags();
        tag1.setTag("tag1");
        Tags tag2 = new Tags();
        tag2.setTag("tag2");

        List<Tags> tags = List.of(tag1, tag2);
        when(tagRepository.findAllByTag(any())).thenReturn(tags);
        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> {
            questionService.addTagsToQuestion(id, request);
        });
    }

    @Test
    public void testMarkAsResolved_Success() throws QuestionNotFoundException, AuthenticationServiceException {
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);
        User user = new User();
        user.setId(1L);
        question.setUser(user);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(user));
        when(questionRepository.updateResolved(questionId)).thenReturn(1);

        int result = questionService.markAsResolved(questionId);
        assertEquals(1, result);
    }

    @Test
    public void testMarkAsResolved_QuestionNotFound() {
        Long questionId = 1L;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> questionService.markAsResolved(questionId));
    }

    @Test
    public void testMarkAsResolved_Unauthorized() {
        Long questionId = 1L;
        Question question = new Question();
        question.setId(questionId);
        User user = new User();
        user.setId(2L);
        question.setUser(user);

        User loggedInUser = new User();
        loggedInUser.setId(3L);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(loggedInUser));

        assertThrows(AuthenticationServiceException.class, () -> questionService.markAsResolved(questionId));
    }


}
