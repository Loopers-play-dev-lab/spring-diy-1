package com.diy.app.infra.viewRender;

import com.diy.app.infra.dto.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JspView implements View {

    private static View instance = null;

    public static View getInstance() {
        if (Objects.isNull(instance)) instance = new JspView();
        return instance;
    }

    public void render(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView modelAndView) throws ServletException, IOException {
        String filePath = getClass().getClassLoader().
                getResource(modelAndView.getViewName() + ".jsp")
                .getFile().split("resources/")[1];

        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(filePath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    public boolean match(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) {
        return Objects.nonNull(getClass().getClassLoader().getResource(modelAndView.getViewName() + ".jsp"));
    }

}
