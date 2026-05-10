package com.diy.framework.web.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JspView {
    private final String viewName;

    public JspView(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            throw new IllegalArgumentException("viewName is required");
        }
        this.viewName = viewName;
    }

    public void render(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(viewName).forward(req, resp);
    }
}
