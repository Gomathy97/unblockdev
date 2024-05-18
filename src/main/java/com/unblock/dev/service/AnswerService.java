package com.unblock.dev.service;

import com.unblock.dev.exception.AnswerNotFoundException;
import com.unblock.dev.exception.CannotAddAnswerToAnswerException;
import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.Answer;
import com.unblock.dev.model.AnswerEntity;
import com.unblock.dev.model.AnswerTagEntity;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.QuestionEntity;
import com.unblock.dev.model.Tags;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.repository.AnswerEntityRepository;
import com.unblock.dev.repository.AnswerRepository;
import com.unblock.dev.repository.AnswerTagEntityRepository;
import com.unblock.dev.repository.QuestionEntityRepository;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.TagRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerEntityRepository answerEntityRepository;

    @Autowired
    QuestionEntityRepository questionEntityRepository;

    @Autowired
    AnswerTagEntityRepository answerTagEntityRepository;

    @Autowired
    ServiceHelper serviceHelper;

    @Transactional
    public Answer answerQuestion(@NonNull Long quesId, Answer answer) throws QuestionNotFoundException, UnprocessableEntityException, AnswerNotFoundException, NoTagsFoundException {
        Optional<Question> question = questionRepository.findById(quesId);
        if (question.isEmpty()) {
            throw new QuestionNotFoundException("Question Not Found");
        }

        insertAnswer(answer);

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setAnswer(answer);
        questionEntity.setQuestion(question.get());

        questionEntityRepository.save(questionEntity);

        return answer;
    }

    @Transactional
    public Answer answerAnswer(@NonNull Long ansId, Answer answer) throws AnswerNotFoundException, UnprocessableEntityException, CannotAddAnswerToAnswerException, NoTagsFoundException {
        Optional<Answer> answerRef = answerRepository.findById(ansId);
        if (answerRef.isEmpty()) {
            throw new AnswerNotFoundException("Answer Not Found");
        }

        Optional<AnswerEntity> isAnAnswer = answerEntityRepository.findByAnswerRefId(ansId);
        if (isAnAnswer.isPresent()) {
            throw new CannotAddAnswerToAnswerException("Cannot add answer to answer");
        }

        insertAnswer(answer);

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answer);
        answerEntity.setAnswerRef(answerRef.get());

        answerEntityRepository.save(answerEntity);
        return answer;
    }

    public Answer getAnswerById(@NonNull Long id) throws AnswerNotFoundException {
        Optional<Answer> results = answerRepository.findById(id);
        if (results.isEmpty()) {
            throw new AnswerNotFoundException("Question not found");
        }

        return results.get();
    }

    @Transactional
    public Boolean addTagsToAnswer(@NonNull Long id, TagsRequest request) throws NoTagsFoundException, AnswerNotFoundException {
        List<Tags> tags = tagRepository.findAllByTag(request.getTags());
        return addTags(id, tags);
    }

    private boolean addTags(Long id, List<Tags> tags) throws NoTagsFoundException, AnswerNotFoundException {
        if (tags.isEmpty()) {
            throw new NoTagsFoundException("No tags found");
        }

        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isEmpty()) {
            throw new AnswerNotFoundException("No question found");
        }

        tags.forEach(tag -> {
            AnswerTagEntity tagEntity = new AnswerTagEntity();
            tagEntity.setTag(tag);
            tagEntity.setAnswer(answer.get());
            try {
                answerTagEntityRepository.save(tagEntity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return true;
    }

    private void setCreateAndUpdateTime(Answer answer) {
        answer.setTimeUpdated(new Date().getTime());
        answer.setTimeCreated(new Date().getTime());
    }

    private Answer setUser(Answer answer) throws UnprocessableEntityException {
        Optional<Answer> result = serviceHelper.getLoggedInUser().map(user -> {
            answer.setUser(user);
            return answer;
        });

        if (result.isEmpty()) {
            throw new UnprocessableEntityException("Unable to update answer");
        }

        return result.get();
    }

    private void insertAnswer(Answer answer) throws UnprocessableEntityException, NoTagsFoundException, AnswerNotFoundException {
        setCreateAndUpdateTime(answer);
        answer = setUser(answer);
        answerRepository.save(answer);

        if (answer.getTags() != null) {
            List<Tags> tags = tagRepository.findAllByTag(answer.getTags());

            if (!addTags(answer.getId(), tags)) {
                throw new NoTagsFoundException("No tags found");
            }
        }
    }

}
