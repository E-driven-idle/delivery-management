package com.driven.dm.user.domain.entity;

import lombok.Builder;

public enum UserRole {
    MASTER("ROLE_MASTER"),
    MANAGER("ROLE_MANAGER"),
    OWNER("ROLE_OWNER"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public boolean isAdmin() {
        return this == MASTER || this == MANAGER;
    }
    public String getAuthority() {
        return authority;
    }
}
