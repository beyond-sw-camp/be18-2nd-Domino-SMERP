package com.domino.smerp.user.constants;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER");

    private String roleName;

    UserRole(String roleName) {

        this.roleName = roleName;
    }
}
