package com.unblock.dev.service;

import com.unblock.dev.exception.CannotAddAnswerToAnswerException;
import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.AnswerEntity;
import com.unblock.dev.model.AnswerTagEntity;
import com.unblock.dev.model.Tags;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.repository.AnswerTagEntityRepository;
import com.unblock.dev.repository.QuestionEntityRepository;
import com.unblock.dev.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.unblock.dev.exception.AnswerNotFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.model.Answer;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.User;
import com.unblock.dev.repository.AnswerEntityRepository;
import com.unblock.dev.repository.AnswerRepository;
import com.unblock.dev.repository.QuestionRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AnswerServiceTest {
    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private AnswerEntityRepository answerEntityRepository;

    @Mock
    private AnswerTagEntityRepository answerTagEntityRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionEntityRepository questionEntityRepository;

    @Mock
    private ServiceHelper serviceHelper;

    @InjectMocks
    private AnswerService answerService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(new User()));
    }

    @Test
    public void testAnswerQuestion_Successful() throws UnprocessableEntityException, AnswerNotFoundException, QuestionNotFoundException, NoTagsFoundException {
        Long quesId = 1L;
        Answer answer = new Answer();
        answer.setUser(new User());
        Question question = new Question();
        when(questionRepository.findById(quesId)).thenReturn(Optional.of(question));

        answerService.answerQuestion(quesId, answer);

        verify(answerRepository, times(1)).save(answer);
    }

    @Test
    public void testAnswerAnswer_Successful() throws UnprocessableEntityException, AnswerNotFoundException, CannotAddAnswerToAnswerException, NoTagsFoundException {
        Long ansId = 1L;
        Answer answer = new Answer();
        answer.setId(ansId);
        when(answerRepository.findById(ansId)).thenReturn(Optional.of(answer));
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswerRef(answer);
        answerEntity.setId(ansId);
        when(answerEntityRepository.findByAnswerRefId(ansId)).thenReturn(Optional.empty());

        answerService.answerAnswer(ansId, answer);

        verify(answerRepository, times(1)).save(answer);
    }

    @Test
    public void testGetAnswerById_Successful() throws AnswerNotFoundException {
        Long id = 1L;
        when(answerRepository.findById(id)).thenReturn(Optional.of(new Answer()));

        Answer result = answerService.getAnswerById(id);

        assertNotNull(result);
    }

    @Test
    public void testAddTagsToAnswer_Successful() throws AnswerNotFoundException, NoTagsFoundException {
        Long id = 1L;
        TagsRequest request = new TagsRequest();
        request.setTags(new String[]{"tag1, tag2"});
        Tags tag1 = new Tags();
        tag1.setTag("tag1");
        Tags tag2 = new Tags();
        tag2.setTag("tag1");
        when(tagRepository.findAllByTag(request.getTags())).thenReturn(List.of(tag1, tag2));
        when(answerRepository.findById(id)).thenReturn(Optional.of(new Answer()));

        boolean result = answerService.addTagsToAnswer(id, request);

        assertTrue(result);
        verify(answerTagEntityRepository, times(2)).save(any(AnswerTagEntity.class));
    }
}
