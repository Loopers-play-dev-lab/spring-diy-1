package com.diy.app.httpSpec;

public enum ContentType {
    JSON("application/json")
    ;
    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
