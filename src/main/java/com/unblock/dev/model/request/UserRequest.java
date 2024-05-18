package com.unblock.dev.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class UserRequest {
    private String fullName;
    private String displayName;
    private String location;
    private String profileImage;
    private String about;
    private String websiteUrl;
    private String twitterUrl;
    private String githubUrl;
}
