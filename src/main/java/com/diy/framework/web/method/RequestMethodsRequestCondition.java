package com.diy.framework.web.method;

import com.diy.framework.web.mvc.anotation.RequestMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RequestMethodsRequestCondition {
    private final Set<RequestMethod> methods;

    public RequestMethodsRequestCondition(final RequestMethod... requestMethods) {
        this.methods = ((requestMethods == null || requestMethods.length == 0) ?
                Collections.emptySet() : new LinkedHashSet<>(Arrays.asList(requestMethods)));
    }

    public Set<RequestMethod> getMethods() {
        return this.methods;
    }
}
