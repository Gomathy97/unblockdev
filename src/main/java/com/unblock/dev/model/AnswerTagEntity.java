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
@Table(name = "ANSWER_TAG_ENTITY",
        indexes = {
                @Index(name = "answer_ref_id3", columnList = "ANSWER_ID"),
                @Index(name = "answer_ref_id3", columnList = "TAG")
        },
        uniqueConstraints = {@UniqueConstraint(columnNames = {"TAG", "ANSWER_ID"})}
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class AnswerTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TAG", referencedColumnName = "TAG")
    private Tags tag;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ANSWER_ID")
    @JsonIgnore
    private Answer answer;

}
