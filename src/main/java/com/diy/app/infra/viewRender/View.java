package com.diy.app.infra.viewRender;

import com.diy.app.infra.dto.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {
    void render(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView modelAndView) throws ServletException, IOException;
    boolean match(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView modelAndView);
}
