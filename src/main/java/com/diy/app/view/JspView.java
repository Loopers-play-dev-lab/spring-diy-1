package com.diy.app.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
    private final String viewPath;

    public JspView(String fullPath) {
        this.viewPath = fullPath; // ex) /lecture-list.jsp
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher(viewPath).forward(req, resp);
    }
}
