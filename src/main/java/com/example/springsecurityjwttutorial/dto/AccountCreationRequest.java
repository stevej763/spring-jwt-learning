package com.example.springsecurityjwttutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreationRequest {

    private final String username;
    private final String password;

    public AccountCreationRequest(@JsonProperty("username") String username,
                                 @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
