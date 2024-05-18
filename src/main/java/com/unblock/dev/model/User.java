package com.unblock.dev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Table (name="USERS")
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonIgnore
    private Long id;

    @Column(name = "FULL_NAME")
    @JsonIgnore
    private String fullName;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "EMAIL")
    @JsonIgnore
    private String email;

    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;

    @Column(name = "TIME_CREATED")
    @JsonIgnore
    private Long timeCreated;

    @Column(name = "TIME_UPDATED")
    @JsonIgnore
    private Long timeUpdated;

    @Column(name = "LOCATION")
    @JsonIgnore
    private String location;

    @Column(name = "PROFILE_IMAGE")
    @JsonIgnore
    private String profileImage;

    @Column(name = "ABOUT")
    @JsonIgnore
    private String about;

    @Column(name = "WEBSITE_URL")
    @JsonIgnore
    private String websiteUrl;

    @Column(name = "TWITTER_URL")
    @JsonIgnore
    private String twitterUrl;

    @Column(name = "GITHUB_URL")
    @JsonIgnore
    private String githubUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private List<Question> questions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private List<Answer> answers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private List<Vote> votes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private List<Tags> tags;

    public User() {}

    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Date getDate(long val) {
        return new Date(val);
    }

    public Date getTimeCreated() {
        return getDate(this.timeCreated);
    }

    public Date getTimeUpdated() {
        return getDate(this.timeUpdated);
    }

}
