package com.diy.framework.web.view;

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
    public void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {
        model.forEach(req::setAttribute);
        req.getRequestDispatcher(viewName).forward(req, res);
    }
}
