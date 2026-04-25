package com.diy.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@FunctionalInterface
public interface Controller {

    ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response, final Map<String, ?> params) throws Exception;
}
