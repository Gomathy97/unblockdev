package com.unblock.dev.service;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.QuestionNotFoundException;
import com.unblock.dev.exception.UnprocessableEntityException;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.QuestionTagEntity;
import com.unblock.dev.model.Tags;
import com.unblock.dev.model.User;
import com.unblock.dev.model.request.TagsRequest;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.QuestionTagEntityRepository;
import com.unblock.dev.repository.TagRepository;
import com.unblock.dev.security.service.UserDetailsImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    QuestionTagEntityRepository questionTagEntityRepository;

    @Autowired
    ServiceHelper serviceHelper;

    @Transactional
    public Question askQuestion(Question question) throws UnprocessableEntityException, QuestionNotFoundException, NoTagsFoundException {
        question.setTimeUpdated(new Date().getTime());
        question.setTimeCreated(new Date().getTime());
        Optional<Question> response = serviceHelper.getLoggedInUser().map(user -> {
            question.setUser(user);
            return questionRepository.save(question);
        });

        if (response.isEmpty()) {
            throw new UnprocessableEntityException("Unable to process the request");
        }

        if (question.getTags() != null) {
            List<Tags> tags = tagRepository.findAllByTag(question.getTags());

            if (!addTags(question.getId(), tags)) {
                throw new NoTagsFoundException("No tags found");
            }

            response = questionRepository.findById(question.getId());
        }

        return response.get();
    }

    public Question getQuestionById(@NonNull Long id) throws QuestionNotFoundException {
        Optional<Question> results = questionRepository.findById(id);
        if (results.isEmpty()) {
            throw new QuestionNotFoundException("Question not found");
        }

        return results.get();
    }

    public List<Question> getQuestionByText(@NonNull String text, int page, int per_page) throws QuestionNotFoundException {
        Pageable pageable = PageRequest.of(page, per_page, Sort.by("timeCreated").descending());
        List<Question> results = questionRepository.findQuestionByText(text, pageable);
        if (results.isEmpty()) {
            throw new QuestionNotFoundException("No Questions found");
        }

        return results;
    }

    public List<Question> getQuestionsOfAUser(@NonNull Long id, int page, int per_page)
            throws UsernameNotFoundException, QuestionNotFoundException {
        Pageable pageable = PageRequest.of(page, per_page, Sort.by("timeCreated").descending());
        List<Question> results =  questionRepository.findByUserId(id, pageable);

        if (results.isEmpty()) {
            throw new QuestionNotFoundException("No Questions found");
        }

        return results;
    }

    @Transactional
    public boolean addTagsToQuestion(@NonNull Long id, TagsRequest request) throws NoTagsFoundException, QuestionNotFoundException {
        List<Tags> tags = tagRepository.findAllByTag(request.getTags());
        return addTags(id, tags);
    }

    public int markAsResolved(@NonNull Long id) throws QuestionNotFoundException {
        Optional<Question> question = questionRepository.findById(id);

        if (question.isEmpty()) {
            throw new QuestionNotFoundException("Question not found");
        }

        User currentLoggedInUser = serviceHelper.getLoggedInUser().get();
        if (!currentLoggedInUser.getId().equals(question.get().getUser().getId())) {
            throw new AuthenticationServiceException("UnAuthorised to resolve the question");
        }
        return questionRepository.updateResolved(id);
    }

    private boolean addTags(Long id, List<Tags> tags) throws NoTagsFoundException, QuestionNotFoundException {
        if (tags.isEmpty()) {
            throw new NoTagsFoundException("No tags found");
        }

        Optional<Question> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            throw new QuestionNotFoundException("No question found");
        }

        tags.forEach(tag -> {
            QuestionTagEntity tagEntity = new QuestionTagEntity();
            tagEntity.setTag(tag);
            tagEntity.setQuestion(question.get());
            try {
                questionTagEntityRepository.save(tagEntity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return true;
    }
}
