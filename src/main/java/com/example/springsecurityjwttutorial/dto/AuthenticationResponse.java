package com.example.springsecurityjwttutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    private final boolean success;
    private final String jwt;
    private final String refreshToken;
    private final String errorMessage;

    public AuthenticationResponse(@JsonProperty("success") boolean success,
                                  @JsonProperty("accessToken") String accessToken,
                                  @JsonProperty("refreshToken") String refreshToken,
                                  @JsonProperty("errorMessage") String errorMessage) {
        this.success = success;
        this.jwt = accessToken;
        this.refreshToken = refreshToken;
        this.errorMessage = errorMessage;
    }

    public String getJwt() {
        return jwt;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
