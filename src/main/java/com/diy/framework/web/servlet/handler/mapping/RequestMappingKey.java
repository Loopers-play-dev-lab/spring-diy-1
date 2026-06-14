package com.diy.framework.web.servlet.handler.mapping;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class RequestMappingKey {

    private final String url;
    private final RequestMethodsRequestCondition methodsCondition;

    public RequestMappingKey(String url, RequestMethodsRequestCondition methodsCondition) {
        this.url = url;
        this.methodsCondition = methodsCondition;
    }

    public boolean matches(HttpServletRequest request) {
        return url.equals(request.getRequestURI()) && methodsCondition.matches(request);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestMappingKey that = (RequestMappingKey) o;
        return Objects.equals(url, that.url) && Objects.equals(methodsCondition.getMethods(), that.methodsCondition.getMethods());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, methodsCondition.getMethods());
    }
}
