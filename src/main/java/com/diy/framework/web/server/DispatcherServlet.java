package com.diy.framework.web.server;

import com.diy.framework.web.Controller;
import com.diy.framework.web.HandlerMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private static HandlerMapping handlerMapping;

    @Override
    public void init() throws ServletException {
        handlerMapping = (HandlerMapping) getServletContext().getAttribute("handlerMapping");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Controller controller = handlerMapping.getHandler(req.getRequestURI());
        if(controller == null) {
            resp.sendError(404);
            return;
        }
        try {
            Objects.requireNonNull(controller).handleRequest(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
