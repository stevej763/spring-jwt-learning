package com.example.springsecurityjwttutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    private final boolean success;
    private final String accessToken;
    private final String refreshToken;
    private final String errorMessage;

    public AuthenticationResponse(@JsonProperty("success") boolean success,
                                  @JsonProperty("accessToken") String accessToken,
                                  @JsonProperty("refreshToken") String refreshToken,
                                  @JsonProperty("errorMessage") String errorMessage) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.errorMessage = errorMessage;
    }

    public String getAccessToken() {
        return accessToken;
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
