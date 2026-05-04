package com.diy.framework.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {
    private final String url;

    public RedirectView(final String url) {
        this.url = url;
    }

    @Override
    public void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res)
            throws Exception {
        res.sendRedirect(url);
    }
}
