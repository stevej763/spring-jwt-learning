package com.example.springsecurityjwttutorial.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Collection;
import java.util.UUID;

public class PersistedUser {

    @Id
    private final UUID id;

    private final String userName;
    private final String password;
    private final Collection<Role> roles;

    public PersistedUser(@JsonProperty("id") UUID id,
                         @JsonProperty("userName") String userName,
                         String password,
                         @JsonProperty("permissions") Collection<Role> roles) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "PersistedUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", roles=" + roles +
                '}';
    }
}
