package com.example.springsecurityjwttutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    private final boolean success;
    private final String jwt;
    private final String errorMessage;

    public AuthenticationResponse(@JsonProperty("success") boolean success,
                                  @JsonProperty("jwt") String jwt,
                                  @JsonProperty("errorMessage") String errorMessage) {
        this.success = success;
        this.jwt = jwt;
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
}
