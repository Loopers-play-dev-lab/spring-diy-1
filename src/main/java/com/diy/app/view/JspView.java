package com.diy.app.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements View {
    private final String viewPath;

    public JspView(String fullPath) {
        this.viewPath = fullPath; // ex) /lecture-list.jsp
    }

    @Override
    public void render(Map<String, Object> model,HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (model != null) {
            model.forEach(req::setAttribute);
        }
        req.getRequestDispatcher(viewPath).forward(req, resp);
    }
}
