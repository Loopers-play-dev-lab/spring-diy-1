package com.diy.framework.web.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
    private final String viewName;

    public JspView(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            throw new IllegalArgumentException("viewName is required");
        }
        this.viewName = "/" + viewName + ".jsp";
    }

    @Override
    public void render(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher(viewName).forward(req, resp);
    }
}
