package com.unblock.dev.service;

import com.unblock.dev.model.Vote;
import com.unblock.dev.model.request.VoteRequest;
import com.unblock.dev.repository.AnswerRepository;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    ServiceHelper serviceHelper;

    @Autowired
    VoteRepository voteRepository;

    public int addVote(VoteRequest voteRequest) {
        if (voteRequest.getQuestion() != null) {
            return incrementQuestionVote(voteRequest.getQuestion(), voteRequest.getVote());
        } else {
            return incrementAnswerVote(voteRequest.getAnswer(), voteRequest.getVote());
        }
    }

    @Transactional
    synchronized int incrementQuestionVote(Long questionId, int voteVal) {
        Vote vote = new Vote();
        vote.setVote(voteVal);
        serviceHelper.getLoggedInUser().map(user -> {
            vote.setUser(user);
            return voteRepository.save(vote);
        });
        return questionRepository.incrementVotes(questionId, voteVal);
    }

    @Transactional
    synchronized int incrementAnswerVote(Long answerId, int voteVal) {
        Vote vote = new Vote();
        vote.setVote(voteVal);
        serviceHelper.getLoggedInUser().map(user -> {
            vote.setUser(user);
            return voteRepository.save(vote);
        });
        return answerRepository.incrementVotes(answerId, voteVal);
    }
}
