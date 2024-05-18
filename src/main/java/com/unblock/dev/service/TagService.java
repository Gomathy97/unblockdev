package com.unblock.dev.service;

import com.unblock.dev.exception.NoTagsFoundException;
import com.unblock.dev.exception.TagCreationException;
import com.unblock.dev.model.Question;
import com.unblock.dev.model.Tags;
import com.unblock.dev.repository.QuestionRepository;
import com.unblock.dev.repository.TagRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ServiceHelper serviceHelper;

    public Tags createTag(@NonNull Tags tag) throws DataIntegrityViolationException, TagCreationException {
        Optional<Tags> response = serviceHelper.getLoggedInUser().map(user -> {
            tag.setUser(user);
            return tagRepository.save(tag);
        });

        if (response.isEmpty()) {
            throw new TagCreationException("Error creating tag");
        }

        return response.get();
    }

    public List<Tags> getTags(@NonNull String text) throws NoTagsFoundException {
        List<Tags> results = tagRepository.findTagsByText(text);
        if (results.isEmpty()) {
            throw new NoTagsFoundException("No Questions found");
        }

        return results;
    }

    public List<Question> getQuestionsByTags(@NonNull String tagValue, int page, int per_page) throws NoTagsFoundException {
        List<Tags> tags = getTags(tagValue);
        List<String> tagNames = tags.stream().map(tag -> tag.getTag()).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, per_page, Sort.by("timeCreated").descending());
        List<Question> questions = questionRepository.findQuestionsByTagsIds(tagNames, pageable);

        return questions;
    }
}
