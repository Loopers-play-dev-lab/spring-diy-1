package com.diy.framework.web.servlet.handler.mapping;

import com.diy.framework.web.mvc.annotation.RequestMethod;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

public class RequestMethodsRequestCondition {

    private final Set<RequestMethod> methods;

    public RequestMethodsRequestCondition(RequestMethod... methods) {
        this.methods = Arrays.stream(methods).collect(Collectors.toSet());
    }

    public Set<RequestMethod> getMethods() {
        return Collections.unmodifiableSet(methods);
    }

    public boolean matches(HttpServletRequest request) {
        if (methods.isEmpty()) {
            return true;
        }

        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return methods.contains(requestMethod);
    }
}
