package com.diy.app.exception;

import com.diy.framework.context.annotation.Component;
import com.diy.framework.web.http.resolver.HandlerExceptionResolver;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class IllegalArgumentExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) throws IOException {
        if (ex instanceof IllegalArgumentException) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            return new ModelAndView(req.getPathInfo());
        }
        return null;
    }
}
