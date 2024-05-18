package com.unblock.dev.controller;

import com.unblock.dev.model.request.ViewRequest;
import com.unblock.dev.model.response.ResponseDetails;
import com.unblock.dev.service.ViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping ("/api")
@RestController
public class ViewController {
    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    ViewService viewService;

    @PostMapping(value = "view")
    public ResponseEntity updateViews(@RequestBody ViewRequest viewRequest) {
        try {
            if (viewService.addVote(viewRequest) == 1)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                throw new RuntimeException("Unable to update vote");
        } catch (RuntimeException e) {
            logger.error("Vote update failed", e);
            return new ResponseEntity<>(new ResponseDetails(e.getMessage(),
                    "vote", viewRequest.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
