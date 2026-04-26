package com.diy.app;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public record JspView(
        String viewName
) implements View {
    @Override
    public void render(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, res);
    }
}
