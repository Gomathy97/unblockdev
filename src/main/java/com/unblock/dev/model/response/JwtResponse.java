package com.unblock.dev.model.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;

    public JwtResponse(String accessToken, String username, String email) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
    }

}
