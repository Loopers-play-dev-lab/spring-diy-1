package com.diy.app.servlet;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    ;

    private final String value;

    HttpHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
