package com.codementor.admin.enums;

import lombok.Getter;

@Getter
public enum SearchType {
    USERNAME("username"),
    EMAIL("email"),
    NICKNAME("nickname"),
    STATUS("status"),
    CREATED_AT("createdAt");

    private final String field;

    SearchType(String field) {
        this.field = field;
    }
}
