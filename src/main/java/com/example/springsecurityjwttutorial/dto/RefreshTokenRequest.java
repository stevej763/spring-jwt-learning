package com.example.springsecurityjwttutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {

    private String refreshToken;

    public RefreshTokenRequest(@JsonProperty("refreshToken") String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
