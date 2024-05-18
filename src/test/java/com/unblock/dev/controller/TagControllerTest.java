package com.unblock.dev.controller;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.TagCreationException;
import com.unblock.dev.model.Tags;
import com.unblock.dev.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @Test
    public void testCreateTag_SuccessfulCreation() throws TagCreationException {
        Tags tag = new Tags();
        when(tagService.createTag(tag)).thenReturn(tag);

        ResponseEntity response = tagController.createTag(tag);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateTag_DataIntegrityViolationException() throws TagCreationException {
        Tags tag = new Tags();
        when(tagService.createTag(tag)).thenThrow(new DataIntegrityViolationException("Duplicate tag"));

        ResponseEntity response = tagController.createTag(tag);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetTags_Successful() throws NoTagsFoundException {
        String search = "example";
        List<Tags> tags = Arrays.asList(new Tags(), new Tags());
        when(tagService.getTags(search)).thenReturn(tags);

        ResponseEntity response = tagController.getTags(search);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testGetTags_NoTagsFoundException() throws NoTagsFoundException {
        String search = "nonexistent";
        when(tagService.getTags(search)).thenThrow(new NoTagsFoundException("No tags found"));

        ResponseEntity response = tagController.getTags(search);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
