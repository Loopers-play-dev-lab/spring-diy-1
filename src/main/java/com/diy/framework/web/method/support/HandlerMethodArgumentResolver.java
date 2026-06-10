package com.diy.framework.web.method.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(final Parameter parameter);

    Object resolveArgument(final Parameter parameter,
                           final HttpServletRequest request,
                           final HttpServletResponse response) throws Exception;
}
