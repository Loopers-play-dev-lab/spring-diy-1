package com.diy.framework.web.server.mv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectView implements View {
    private final String viewName;

    public RedirectView(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            throw new IllegalArgumentException("viewName is required");
        }
        this.viewName = viewName;
    }

    @Override
    public void render(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView mav) throws Exception {
        System.out.println("[RedirectView] render is called. redirect to " + viewName);
        resp.sendRedirect(viewName);
    }
}
