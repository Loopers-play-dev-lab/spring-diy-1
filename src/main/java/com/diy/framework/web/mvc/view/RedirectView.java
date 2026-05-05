package com.diy.framework.web.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {
    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse res, Map<String, Object> model) throws Exception {
        res.sendRedirect(url);
    }
}
