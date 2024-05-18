package com.unblock.dev.controller;

import com.unblock.dev.exception.AnswerNotFoundException;
import com.unblock.dev.exception.CannotAddAnswerToAnswerException;
import com.unblock.dev.exception.InvalidIdentiferException;
import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.Answer;
import com.unblock.dev.model.response.ResponseDetails;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.service.AnswerService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
public class AnswerController {
    private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

    @Autowired
    AnswerService answerService;

    @PostMapping(value = "question/{id}/answer")
    public ResponseEntity answerQuestion(@NonNull @PathVariable("id") Long quesId, @RequestBody Answer answer) {
        try {
            Answer answerResponse = answerService.answerQuestion(quesId, answer);
            return new ResponseEntity(answerResponse, HttpStatus.CREATED);
        } catch (QuestionNotFoundException e) {
            logger.error("Error ", e);
            ResponseDetails response = new ResponseDetails(e.getMessage(), "id", quesId.toString());
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        } catch (AnswerNotFoundException | NoTagsFoundException e) {
            logger.error("Tags not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/tags", Arrays.toString(answer.getTags())), HttpStatus.NOT_FOUND);
        }catch (UnprocessableEntityException e) {
            logger.error("Error ", e);
            ResponseDetails response = new ResponseDetails(e.getMessage(), "id", quesId.toString());
            return new ResponseEntity(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping(value = "answer/{id}/answer")
    public ResponseEntity answerAnswer(@PathVariable("id") Long ansId, @RequestBody Answer answer) {
        try {
            if (ObjectUtils.isEmpty(ansId)) {
                throw new InvalidIdentiferException("Provided id is not valid");
            }
            Answer answerResponse = answerService.answerAnswer(ansId, answer);
            return new ResponseEntity<>(answerResponse, HttpStatus.CREATED);
        } catch (AnswerNotFoundException e) {
            logger.error("Error ", e);
            ResponseDetails response = new ResponseDetails(e.getMessage(), "id", String.valueOf(ansId));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (UnprocessableEntityException | CannotAddAnswerToAnswerException e) {
            logger.error("Error ", e);
            ResponseDetails response = new ResponseDetails(e.getMessage(), "id", String.valueOf(ansId));
            return new ResponseEntity(response, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (InvalidIdentiferException e) {
            logger.error("Error ", e);
            ResponseDetails response = new ResponseDetails(e.getMessage(), "id", String.valueOf(ansId));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        } catch (NoTagsFoundException e) {
            logger.error("Error ", e);
            ResponseDetails response = new ResponseDetails(e.getMessage(),
                    "tags", Arrays.toString(answer.getTags()));
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "answer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAnswerById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity(answerService.getAnswerById(id), HttpStatus.OK);
        } catch (AnswerNotFoundException e) {
            logger.error("Answer not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Get Answer failed", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "answer/{id}/tags")
    ResponseEntity addTagsToQuestion(@PathVariable("id") Long id, @RequestBody TagsRequest tagsRequest) {
        try {
            if (answerService.addTagsToAnswer(id, tagsRequest))
                return new ResponseEntity(HttpStatus.OK);
            else
                throw new RuntimeException("Unable to update the tags");
        } catch (NoTagsFoundException e) {
            logger.error("Tags not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "tags", id.toString()), HttpStatus.NOT_FOUND);
        } catch (AnswerNotFoundException e) {
            logger.error("Answer not found", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            logger.error("Tag update failed", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "/id", id.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
