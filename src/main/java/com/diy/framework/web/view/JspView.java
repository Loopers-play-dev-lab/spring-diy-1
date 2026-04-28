package com.diy.framework.web.view;

import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {
    private final String viewName;

    public JspView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        model.forEach(req::setAttribute);
        final RequestDispatcher dispatcher = req.getRequestDispatcher(viewName);
        dispatcher.forward(req, resp);
    }
}
