package com.unblock.dev.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table (name = "QUESTIONS",
        indexes = {
        @Index(name = "title_IDX1",  columnList="title")
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column (name = "TITLE")
    private String title;

    @Column (name = "DESCRIPTION")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column (name = "IMAGES")
    private String[] images;

    @Column (name = "VIDEOS")
    private String[] videos;

    @Column (name = "RESOLVED")
    private Boolean resolved = false;

    @Column(name = "TIME_CREATED")
    private Long timeCreated;

    @Column(name = "TIME_UPDATED")
    private Long timeUpdated;

    @Column(name = "VOTES")
    private Long votes = 0L;

    @Column(name = "VIEWS")
    private Long views = 0L;

    @Transient
    private String[] tags;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("question")
    private List<QuestionEntity> answers;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("question")
    @JsonIgnore
    private QuestionScore questionScore;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("question")
    private List<QuestionTagEntity> tagEntities;

    public Date getDate(long val) {
        return new Date(val);
    }

    public Date getTimeCreated() {
        return getDate(this.timeCreated);
    }

    public Date getTimeUpdated() {
        return getDate(this.timeUpdated);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : "null") +
                '}';
    }

}
