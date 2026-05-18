package com.diy.framework.enums;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod equals(String compare) {
        return valueOf(compare);
    }
}
