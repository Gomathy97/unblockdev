package com.unblock.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "QUESTIONS_SCORE")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QuestionScore {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @Column(name = "NEWEST_SCORE")
    private String newestScore;

    @Column(name = "ACTIVE_SCORE")
    private String activeScore;

}
