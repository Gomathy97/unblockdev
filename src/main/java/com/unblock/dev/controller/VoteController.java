package com.unblock.dev.controller;

import com.unblock.dev.model.request.VoteRequest;
import com.unblock.dev.model.response.ResponseDetails;
import com.unblock.dev.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VoteController {

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    VoteService voteService;

    @PostMapping(value = "vote")
    public ResponseEntity addVote(@RequestBody VoteRequest voteRequest) {
        try {
            if (voteService.addVote(voteRequest) == 1)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                throw new RuntimeException("Unable to update vote");
        } catch (RuntimeException e) {
            logger.error("Vote update failed", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "vote", voteRequest.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
