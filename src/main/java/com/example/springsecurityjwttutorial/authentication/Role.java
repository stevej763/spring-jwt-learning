package com.example.springsecurityjwttutorial.authentication;

import java.util.Collections;
import java.util.Set;

public enum Role {

    DEMO_USER(Collections.emptySet()),
    USER(Set.of(Permission.CAN_FACILITATE, Permission.CAN_MONITOR)),
    INSTITUTION_ADMIN(Set.of(Permission.CAN_FACILITATE, Permission.CAN_MONITOR, Permission.CAN_INVITE_USER, Permission.CAN_REMOVE_USER)),
    APPLICATION_ADMIN(Set.of(Permission.ALL_ACCESS));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
