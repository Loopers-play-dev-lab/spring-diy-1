package com.diy.framework.web.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RedirectView implements View {
    private final String redirectUrl;

    public RedirectView(final String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(redirectUrl);
    }
}
