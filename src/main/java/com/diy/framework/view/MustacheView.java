package com.diy.framework.view;

import com.diy.framework.value.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public record MustacheView(
    String viewName
) implements View {

    @Override
    public void render(Model model, final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        if (model != null) {
            model.attributes().forEach(req::setAttribute);
        }
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        requestDispatcher.forward(req, res);
    }
}
