package com.diy.framework.web.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface View {
    void render(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView mav) throws Exception;
}
