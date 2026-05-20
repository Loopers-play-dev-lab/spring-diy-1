package com.diy.framework.context.annotation;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public boolean equals(String method) {
        return method != null && this.name().equalsIgnoreCase(method.trim());
    }
}
