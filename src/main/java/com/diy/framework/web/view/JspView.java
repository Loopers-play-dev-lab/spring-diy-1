package com.diy.framework.web.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View{
    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp) throws Exception{
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        model.forEach(req::setAttribute);
        requestDispatcher.forward(req, resp);
    }
}