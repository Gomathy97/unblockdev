package com.unblock.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "TAGS",
        indexes = {
                @Index(name = "tag_name_id1", columnList = "TAG"),
        })
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    @Column(name = "TAG", unique = true, nullable = false)
    private String tag;

    @Column (name = "TAG_DESCRIPTION")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CREATED_BY", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("tag")
    @JsonIgnore
    private List<QuestionTagEntity> questionTagEntities;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("tag")
    @JsonIgnore
    private List<AnswerTagEntity> answerTagEntities;
}
