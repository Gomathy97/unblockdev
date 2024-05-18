package com.unblock.dev.controller;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.response.ResponseDetails;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.service.QuestionService;
import com.unblock.dev.service.TagService;
import jakarta.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class QuestionsController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionsController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    TagService tagService;

    @PostMapping(value = "/question", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity askQuestion(@RequestBody Question question) {
        try {
            return new ResponseEntity<>(questionService.askQuestion(question), HttpStatus.CREATED);
        } catch (UnprocessableEntityException e) {
            logger.error("Ask Question failed", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (QuestionNotFoundException | NoTagsFoundException e) {
            logger.error("Tags not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/tags", Arrays.toString(question.getTags())), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Ask Question failed", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/question/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getQuestionById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity(questionService.getQuestionById(id), HttpStatus.OK);
        } catch (QuestionNotFoundException e) {
            logger.error("Get Question not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Get Question failed", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getQuestionByText(@QueryParam("search") String search, @QueryParam("tag") String tag,
                                     @QueryParam("page") int page, @QueryParam("per_page") int per_page) {
        try {
            if (search != null) {
                return new ResponseEntity(questionService.getQuestionByText(search, page, per_page), HttpStatus.OK);
            } else {
                return new ResponseEntity(tagService.getQuestionsByTags(tag, page, per_page), HttpStatus.OK);
            }
        } catch (QuestionNotFoundException e) {
            logger.error("Get Question not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/search", search), HttpStatus.NOT_FOUND);
        } catch (NoTagsFoundException e) {
            logger.error("No Tags found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/tag", tag), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "questions/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getQuestionsOfAUser(@PathVariable("id") Long id,
                                       @QueryParam("page") int page, @QueryParam("per_page") int per_page) {
        try {
            return new ResponseEntity(questionService.getQuestionsOfAUser(id, page, per_page), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "question/{id}/user", id.toString()), HttpStatus.NOT_FOUND);
        } catch (QuestionNotFoundException e) {
            logger.error("Get Question not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Get Questions for a user failed", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "question/{id}/tags")
    ResponseEntity addTagsToQuestion(@PathVariable("id") Long id, @RequestBody TagsRequest tagsRequest) {
        try {
            if (questionService.addTagsToQuestion(id, tagsRequest))
                return new ResponseEntity(HttpStatus.OK);
            else
                throw new RuntimeException("Unable to update the tags");
        } catch (NoTagsFoundException e) {
            logger.error("Tags not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "tags", id.toString()), HttpStatus.NOT_FOUND);
        } catch (QuestionNotFoundException e) {
            logger.error("Question not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            logger.error("Tag update failed", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping(value = "question/{id}/resolved")
    ResponseEntity resolveQuestion(@PathVariable("id") Long id) {
        try {
            if (questionService.markAsResolved(id) == 1)
                return new ResponseEntity(HttpStatus.OK);
            else
                throw new RuntimeException("Unable to update the tags");
        } catch (QuestionNotFoundException e) {
            logger.error("Question not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.NOT_FOUND);
        } catch (AuthenticationServiceException e) {
            logger.error("Not Authorized", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "Authorization", null), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            logger.error("Tag update failed", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
