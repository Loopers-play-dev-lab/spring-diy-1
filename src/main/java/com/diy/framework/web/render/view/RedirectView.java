package com.diy.framework.web.render.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RedirectView implements View {
    private final String viewName;

    public RedirectView(final String viewName) {
        this.viewName = viewName.replace("redirect:", "");
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(viewName);
    }
}
