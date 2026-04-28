package com.diy.framework.web.render.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JspView implements View {
    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName + ".jsp";
    }

    @Override
    public void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        try {
            for(Map.Entry<String, Object> entrySet : model.entrySet()) {
                req.setAttribute(entrySet.getKey(), entrySet.getValue());
            }
            requestDispatcher.forward(req, res);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
