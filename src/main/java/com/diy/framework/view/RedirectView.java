package com.diy.framework.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RedirectView implements View {

    private final String location;

    public RedirectView(String location) {
        this.location = location;
    }

    @Override
    public void render(final Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect(location);
    }
}
