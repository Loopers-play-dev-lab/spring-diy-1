package com.diy.app.render;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RedirectView implements View {

    private final String viewName;

    public RedirectView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect(viewName);
    }
}
