package com.diy.app.infra.viewRender;

import com.diy.app.infra.dto.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class RedirectView implements View {

    private static View instance = null;

    public static View getInstance() {
        if (Objects.isNull(instance)) instance = new RedirectView();
        return instance;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws ServletException, IOException {
        resp.sendRedirect(modelAndView.getViewName().substring(9));
    }

    @Override
    public boolean match(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) {
        return modelAndView.getViewName().startsWith("redirect:");
    }

}
