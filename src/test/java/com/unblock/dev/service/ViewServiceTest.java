package com.unblock.dev.service;

import com.unblock.dev.model.request.ViewRequest;
import com.unblock.dev.repository.AnswerRepository;
import com.unblock.dev.repository.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ViewServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private ViewService viewService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddVote_IncrementQuestionVote() {
        ViewRequest viewRequest = new ViewRequest();
        viewRequest.setQuestion(1L);
        viewRequest.setView(1);

        when(questionRepository.incrementViews(viewRequest.getQuestion(), viewRequest.getView())).thenReturn(1);

        int result = viewService.addVote(viewRequest);

        assertEquals(1, result);
        verify(questionRepository, times(1)).incrementViews(viewRequest.getQuestion(), viewRequest.getView());
    }

    @Test
    public void testAddVote_IncrementAnswerVote() {
        ViewRequest viewRequest = new ViewRequest();
        viewRequest.setAnswer(1L);
        viewRequest.setView(1);

        when(answerRepository.incrementViews(viewRequest.getAnswer(), viewRequest.getView())).thenReturn(1);

        int result = viewService.addVote(viewRequest);

        assertEquals(1, result);
        verify(answerRepository, times(1)).incrementViews(viewRequest.getAnswer(), viewRequest.getView());
    }

    @Test
    public void testIncrementQuestionVote() {
        Long questionId = 1L;
        int voteVal = 1;

        when(questionRepository.incrementViews(questionId, voteVal)).thenReturn(1);

        int result = viewService.incrementQuestionVote(questionId, voteVal);

        assertEquals(1, result);
        verify(questionRepository, times(1)).incrementViews(questionId, voteVal);
    }

    @Test
    public void testIncrementAnswerVote() {
        Long answerId = 1L;
        int voteVal = 1;

        when(answerRepository.incrementViews(answerId, voteVal)).thenReturn(1);

        int result = viewService.incrementAnswerVote(answerId, voteVal);

        assertEquals(1, result);
        verify(answerRepository, times(1)).incrementViews(answerId, voteVal);
    }
}
