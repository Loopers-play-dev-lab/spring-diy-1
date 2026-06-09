package com.diy.framework.web.mvc.method;

import com.diy.framework.web.method.support.HandlerMethodArgumentResolver;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class ServletResponseMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final Parameter parameter) {
        return ServletResponse.class.isAssignableFrom(parameter.getType());
    }

    @Override
    public Object resolveArgument(final Parameter parameter,
                                  final HttpServletRequest request,
                                  final HttpServletResponse response) {
        return response;
    }
}
