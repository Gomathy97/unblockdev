package com.unblock.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "QUESTION_ENTITY",
        indexes = {
                @Index(name = "question_ref_id1", columnList = "QUESTION_REF_ID"),
        })
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "QUESTION_REF_ID")
    @JsonIgnore
    private Question question;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;
}
