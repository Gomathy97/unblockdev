package com.unblock.dev.controller;

import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.model.response.ResponseDetails;
import com.unblock.dev.service.QuestionScoreService;
import jakarta.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/question/score")
public class QuestionScoreController {

    public static Logger logger = LoggerFactory.getLogger(QuestionScoreController.class);

    @Autowired
    QuestionScoreService questionScoreService;

    @PutMapping()
    public ResponseEntity getQuestionForQuestion(@RequestBody long[] questions) {
        try {
            if (questionScoreService.updateScores(questions)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new RuntimeException("Unable to update score");
            }
        } catch (QuestionNotFoundException e) {
            logger.error("Question not found ", e);
            return new ResponseEntity(new ResponseDetails(e.getMessage(), "questions", Arrays.toString(questions)), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity getQuestionsByFilter(@QueryParam("filter") String filter, int page, int per_page) {
        try {
            return new ResponseEntity<>(questionScoreService.getTopQuestions(filter, page, per_page), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ResponseDetails(e.getMessage(), "filter", filter), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
