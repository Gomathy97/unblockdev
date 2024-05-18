package com.unblock.dev.service;

import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.QuestionScore;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.QuestionScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionScoreService {

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    @Autowired
    QuestionScoreRepository questionScoreRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Transactional
    public boolean updateScores(long[] ids) throws QuestionNotFoundException {
        for (long id : ids) {
            Optional<Question> questionOptional = questionRepository.findById(id);

            if (questionOptional.isEmpty()) {
                throw new QuestionNotFoundException("Question Not Found");
            }

            Question question = questionOptional.get();
            QuestionScore score = new QuestionScore();
            score.setQuestion(question);

            Long numerator = question.getViews() * question.getVotes() * (question.getResolved() ? 0 : 1);
            int newDenomiator = (new Date().getMinutes() - question.getTimeCreated().getMinutes()) + new Date().getMinutes();
            int activeDenomiator = (new Date().getMinutes() - question.getTimeUpdated().getMinutes());

            double newestS = ((double) (numerator)) / newDenomiator;
            double activeS = ((double) (numerator)) / activeDenomiator;

            String active = decfor.format(activeS);
            String newest = decfor.format(newestS);

            score.setActiveScore(active);
            score.setNewestScore(newest);

            Optional<QuestionScore> questionScoreOptional = questionScoreRepository.findByQuestionId(id);

            if (questionScoreOptional.isEmpty()) {
                questionScoreRepository.save(score);
            }

            questionScoreRepository.updateScore(active, newest, questionScoreOptional.get().getId());
        }
        return true;
    }

    public List<QuestionScore> getTopQuestions(String filter, int page, int per_page) {
        Pageable pageable = PageRequest.of(page, per_page, Sort.by(filter + "Score").descending());
        return questionScoreRepository.findByFilter(filter + "Score", pageable);
    }
}
