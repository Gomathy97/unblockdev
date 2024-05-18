package com.unblock.dev.controller;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.TagCreationException;
import com.unblock.dev.model.response.ResponseDetails;
import com.unblock.dev.model.Tags;
import com.unblock.dev.service.TagService;
import jakarta.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class TagController {
    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    TagService tagService;

    @PostMapping(value = "tag")
    public ResponseEntity createTag(@RequestBody Tags tag) {
        try {
            Tags response = tagService.createTag(tag);
            return new ResponseEntity(response, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error ", e);
            return new ResponseEntity(new ResponseDetails(e.getMessage(), "tag", tag.getTag()),
                    HttpStatus.BAD_REQUEST);
        } catch (TagCreationException e) {
            logger.error("Error ", e);
            return new ResponseEntity(new ResponseDetails(e.getMessage(), "tag", tag.getTag()),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping(value = "tag")
    public ResponseEntity getTags(@QueryParam("search") String search) {
        try {
            List<Tags> response = tagService.getTags(search);
            return new ResponseEntity(response, HttpStatus.CREATED);
        } catch (NoTagsFoundException e) {
            logger.error("Error ", e);
            return new ResponseEntity(new ResponseDetails(e.getMessage(), "search", search),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
