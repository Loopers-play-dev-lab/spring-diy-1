package com.diy.framework.web.http.resolver;

import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HandlerExceptionResolver {
    ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) throws IOException;
}
