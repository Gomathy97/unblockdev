package com.unblock.dev.service;

import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.QuestionScore;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.QuestionScoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionScoreServiceTest {
    @Mock
    private QuestionScoreRepository questionScoreRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionScoreService questionScoreService;

    @Before
    public void setUp() {
        questionScoreService = new QuestionScoreService();
        questionScoreService.questionScoreRepository = questionScoreRepository;
        questionScoreService.questionRepository = questionRepository;
    }

    @Test
    public void testUpdateScores_Success() throws QuestionNotFoundException {
        long[] ids = {1L, 2L};

        Question question1 = new Question();
        question1.setId(1L);
        question1.setViews(100L);
        question1.setVotes(10L);
        question1.setResolved(false);
        question1.setTimeCreated(new Date(System.currentTimeMillis() - 60000).getTime());
        question1.setTimeUpdated(new Date(System.currentTimeMillis() - 30000).getTime());

        Question question2 = new Question();
        question2.setId(2L);
        question2.setViews(150L);
        question2.setVotes(15L);
        question2.setResolved(false);
        question2.setTimeCreated(new Date(System.currentTimeMillis() - 120000).getTime());
        question2.setTimeUpdated(new Date(System.currentTimeMillis() - 60000).getTime());

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question1));
        when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));

        QuestionScore questionScore1 = new QuestionScore();
        questionScore1.setQuestion(question1);

        QuestionScore questionScore2 = new QuestionScore();
        questionScore2.setQuestion(question2);

        when(questionScoreRepository.findByQuestionId(1L)).thenReturn(Optional.of(questionScore1));
        when(questionScoreRepository.findByQuestionId(2L)).thenReturn(Optional.of(questionScore2));

        boolean result = questionScoreService.updateScores(ids);

        assertTrue(result);
    }

    @Test
    public void testUpdateScores_QuestionNotFound() {
        long[] ids = {1L};

        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> questionScoreService.updateScores(ids));
    }

    @Test
    public void testGetTopQuestions_Success() {
        String filter = "newest";
        int page = 0;
        int per_page = 10;
        Pageable pageable = PageRequest.of(page, per_page, Sort.by(filter + "Score").descending());

        QuestionScore questionScore = new QuestionScore();
        questionScore.setNewestScore("10.00");
        List<QuestionScore> questionScores = List.of(questionScore);

        when(questionScoreRepository.findByFilter(filter + "Score", pageable)).thenReturn(questionScores);

        List<QuestionScore> result = questionScoreService.getTopQuestions(filter, page, per_page);

        assertEquals(1, result.size());
        assertEquals("10.00", result.get(0).getNewestScore());
    }
}
