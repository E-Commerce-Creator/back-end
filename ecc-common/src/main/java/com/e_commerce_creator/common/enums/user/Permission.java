package com.e_commerce_creator.common.enums.user;

public enum Permission {
    ADMIN_READ("admin::read"),
    ADMIN_UPDATE("admin::update"),
    ADMIN_CREATE("admin::create"),
    ADMIN_DELETE("admin::delete"),

    OWNER_READ("owner::read"),
    OWNER_UPDATE("owner::update"),
    OWNER_CREATE("owner::create"),
    OWNER_DELETE("owner::delete");

    private final String operation;

    Permission(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
