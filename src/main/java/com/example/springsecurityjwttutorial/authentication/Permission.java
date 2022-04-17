package com.example.springsecurityjwttutorial.authentication;

public enum Permission {

    ALL_ACCESS("ALL_ACCESS"),
    CAN_INVITE_USER("CAN_INVITE_USER"),
    CAN_REMOVE_USER("CAN_REMOVE_USER"),
    CAN_FACILITATE("CAN_FACILITATE"),
    CAN_MONITOR("CAN_MONITOR");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
