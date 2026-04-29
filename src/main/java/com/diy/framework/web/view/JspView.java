package com.diy.framework.web.view;

import com.diy.framework.web.model.Model;
import java.io.IOException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
    private final String viewName;
    private final Model model;

    public JspView(final String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    @Override
    public void render(final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException {
        Map<String, Object> modelMap = model.getModel();
        modelMap.forEach((key, value) -> {
            req.setAttribute(key, value);
        });

        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, res);
    }
}