package com.diy.framework.web;

import java.util.Map;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    private static Map<String, HttpMethod> allMethods = Map.of(
            "GET", GET,
            "POST", POST,
            "PUT", PUT,
            "DELETE", DELETE
    );

    public static HttpMethod from(String name) {
        return allMethods.get(name);
    }
}
