package com.unblock.dev.service;

import com.unblock.dev.model.request.ViewRequest;
import com.unblock.dev.repository.AnswerRepository;
import com.unblock.dev.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViewService {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    public int addVote(ViewRequest viewRequest) {
        if (viewRequest.getQuestion() != null) {
            return incrementQuestionVote(viewRequest.getQuestion(), viewRequest.getView());
        } else {
            return incrementAnswerVote(viewRequest.getAnswer(), viewRequest.getView());
        }
    }

    @Transactional
    synchronized int incrementQuestionVote(Long questionId, int voteVal) {
        return questionRepository.incrementViews(questionId, voteVal);
    }

    @Transactional
    synchronized int incrementAnswerVote(Long answerId, int voteVal) {
        return answerRepository.incrementViews(answerId, voteVal);
    }
}
