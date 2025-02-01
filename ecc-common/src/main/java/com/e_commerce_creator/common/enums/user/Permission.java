package com.e_commerce_creator.common.enums.user;

public enum Permission {
    USER_READ("user_read"),
    USER_UPDATE("user_update"),
    USER_CREATE("user_create"),
    USER_DELETE("user_delete"),
    WORKER_READ("worker_read"),
    WORKER_UPDATE("worker_update"),
    WORKER_CREATE("worker_create"),
    WORKER_DELETE("worker_delete"),
    OWNER_READ("owner_read"),
    OWNER_UPDATE("owner_update"),
    OWNER_CREATE("owner_create"),
    OWNER_DELETE("owner_delete"),
    ADMIN_READ("admin_read"),
    ADMIN_UPDATE("admin_update"),
    ADMIN_CREATE("admin_create"),
    ADMIN_DELETE("admin_delete");


    private final String operation;

    Permission(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
