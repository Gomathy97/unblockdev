package com.unblock.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import lombok.Data;

@Entity
@Table(name = "ANSWER_ENTITY",
        indexes = {
                @Index(name = "answer_ref_id1", columnList = "ANSWER_REF_ID")
        })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ANSWER_REF_ID")
    @JsonIgnore
    private Answer answerRef;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ANSWER_ID")
    private Answer answer;

}
