package com.diy.framework.web.view;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView {
    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    public void render(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, res);
    }
}