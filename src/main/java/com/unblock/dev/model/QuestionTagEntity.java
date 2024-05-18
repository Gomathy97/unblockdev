package com.unblock.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(name = "QUESTION_TAG_ENTITY",
        indexes = {
                @Index(name = "question_ref_id3", columnList = "QUESTION_ID"),
                @Index(name = "question_ref_id3", columnList = "TAG")
        },
        uniqueConstraints = {@UniqueConstraint(columnNames = {"TAG", "QUESTION_ID"})})
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TAG", referencedColumnName = "TAG")
    private Tags tag;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "QUESTION_ID")
    @JsonIgnore
    private Question question;

}
