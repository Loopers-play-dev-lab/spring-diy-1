package com.diy.app.servlet;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PATCH("PATCH"),
    PUT("PUT"),
    DELETE("DELETE"),
    ;

    private final String value;

    private HttpMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
