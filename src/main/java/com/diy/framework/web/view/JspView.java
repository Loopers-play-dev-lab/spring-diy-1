package com.diy.framework.web.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspView implements View {
    private final String viewName;
    private final Boolean isRedirect;

    public JspView(String viewName) {
        this.viewName = viewName;
        this.isRedirect = false;
    }

    public JspView(String viewName, Boolean isRedirect) {
        this.viewName = viewName;
        this.isRedirect = isRedirect;
    }

    @Override
    public void render(Map<String, Object> mav, final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        if (isRedirect) {
            res.sendRedirect(viewName);
        }
        else {
            final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
            requestDispatcher.forward(req, res);
        }

    }

}
