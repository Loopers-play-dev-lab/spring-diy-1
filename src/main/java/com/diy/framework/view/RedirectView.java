package com.diy.framework.view;

import com.diy.framework.value.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public record RedirectView(
        String url
) implements View {
    @Override
    public void render(Model model, final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        res.sendRedirect(url);
    }
}
