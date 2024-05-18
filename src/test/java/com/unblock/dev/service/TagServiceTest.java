package com.unblock.dev.service;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.TagCreationException;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.Tags;
import com.unblock.dev.model.User;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ServiceHelper serviceHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTag_Success() throws DataIntegrityViolationException, TagCreationException {
        Tags tag = new Tags();
        tag.setTag("sample-tag");

        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(new User()));
        when(tagRepository.save(tag)).thenReturn(tag);

        Tags result = tagService.createTag(tag);
        assertNotNull(result);
        verify(tagRepository, times(1)).save(tag);
    }

    @Test
    public void testCreateTag_DataIntegrityViolationException() {
        Tags tag = new Tags();
        tag.setTag("sample-tag");

        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.of(new User()));
        when(tagRepository.save(tag)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> {
            tagService.createTag(tag);
        });
    }

    @Test
    public void testCreateTag_TagCreationFailed() {
        Tags tag = new Tags();
        tag.setTag("sample-tag");

        when(serviceHelper.getLoggedInUser()).thenReturn(Optional.empty());

        assertThrows(TagCreationException.class, () -> {
            tagService.createTag(tag);
        });
    }

    @Test
    public void testGetTags_Success() throws NoTagsFoundException {
        String searchText = "sample-tag";
        Tags tag = new Tags();
        tag.setTag("sample-tag");
        List<Tags> tags = List.of(tag);

        when(tagRepository.findTagsByText(searchText)).thenReturn(tags);

        List<Tags> result = tagService.getTags(searchText);
        assertEquals(1, result.size());
        verify(tagRepository, times(1)).findTagsByText(searchText);
    }

    @Test
    public void testGetTags_NoTagsFoundException() {
        String searchText = "sample-tag";

        when(tagRepository.findTagsByText(searchText)).thenReturn(Collections.emptyList());

        assertThrows(NoTagsFoundException.class, () -> {
            tagService.getTags(searchText);
        });
    }

    @Test
    public void testGetQuestionsByTags_Success() throws NoTagsFoundException {
        String tagValue = "sample-tag";
        Tags tag = new Tags();
        tag.setTag("sample-tag");
        List<Tags> tags = List.of(tag);
        List<Question> questions = List.of(new Question());

        when(tagRepository.findTagsByText(tagValue)).thenReturn(tags);
        when(questionRepository.findQuestionsByTagsIds(anyList(), any(Pageable.class))).thenReturn(questions);

        List<Question> result = tagService.getQuestionsByTags(tagValue, 0, 10);
        assertEquals(1, result.size());
        verify(tagRepository, times(1)).findTagsByText(tagValue);
        verify(questionRepository, times(1)).findQuestionsByTagsIds(anyList(), any(Pageable.class));
    }

    @Test
    public void testGetQuestionsByTags_NoTagsFoundException() {
        String tagValue = "sample-tag";

        when(tagRepository.findTagsByText(tagValue)).thenReturn(Collections.emptyList());

        assertThrows(NoTagsFoundException.class, () -> {
            tagService.getQuestionsByTags(tagValue, 0, 10);
        });
    }
}
