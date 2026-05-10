package com.diy.framework.web.mvc.servlet;

import java.util.Map;

@FunctionalInterface
public interface RestController {
    Object handleRequest(String method, Map<String, ?> params) throws IllegalArgumentException;
}
