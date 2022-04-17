package com.example.springsecurityjwttutorial.authentication;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class PersistedUser {

    @Id
    private final UUID id;

    private final String userName;
    private final String password;

    public PersistedUser(UUID id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "PersistedUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
