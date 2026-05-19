package com.diy.framework.web;

import java.util.Map;

public enum HttpRequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    PATCH("PATCH"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE")
    ;

    final String name;

    HttpRequestMethod(String name) {
        this.name = name;
    }

    private static Map<String, HttpRequestMethod> allMethods = Map.of(
            "GET", GET,
            "POST", POST,
            "PUT", PUT,
            "DELETE", DELETE,
            "HEAD", HEAD,
            "PATCH", PATCH,
            "OPTIONS", OPTIONS,
            "TRACE", TRACE
    );

    public static HttpRequestMethod from(String name) {
        return allMethods.get(name);
    }
}
