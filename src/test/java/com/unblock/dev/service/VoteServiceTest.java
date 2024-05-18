package com.unblock.dev.service;

import com.unblock.dev.model.User;
import com.unblock.dev.model.Vote;
import com.unblock.dev.model.request.VoteRequest;
import com.unblock.dev.repository.AnswerRepository;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.VoteRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private ServiceHelper serviceHelper;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteService voteService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddVote_ToQuestion() {
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setQuestion(1L);
        voteRequest.setVote(1);

        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(new User()));

        voteService.addVote(voteRequest);

        verify(questionRepository, times(1)).incrementVotes(1L, 1);
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    public void testAddVote_ToAnswer() {
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.setAnswer(1L);
        voteRequest.setVote(1);

        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(new User()));

        voteService.addVote(voteRequest);

        verify(answerRepository, times(1)).incrementVotes(1L, 1);
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    public void testIncrementQuestionVote() {
        Long questionId = 1L;
        int voteVal = 1;
        Vote vote = new Vote();
        vote.setVote(voteVal);

        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(new User()));
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        voteService.incrementQuestionVote(questionId, voteVal);

        verify(questionRepository, times(1)).incrementVotes(questionId, voteVal);
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    public void testIncrementAnswerVote() {
        Long answerId = 1L;
        int voteVal = 1;
        Vote vote = new Vote();
        vote.setVote(voteVal);

        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(new User()));
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        voteService.incrementAnswerVote(answerId, voteVal);

        verify(answerRepository, times(1)).incrementVotes(answerId, voteVal);
        verify(voteRepository, times(1)).save(any(Vote.class));
    }
}
