package com.diy.framework.web.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View {

    public static final String PREFIX = "redirect:";

    private final String location;

    public RedirectView(String location) {
        this.location = location;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(location);
    }
}
