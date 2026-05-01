package com.diy.framework.web.mvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspView implements View {
    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        try {
            model.forEach(req::setAttribute);
            requestDispatcher.forward(req, res);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
