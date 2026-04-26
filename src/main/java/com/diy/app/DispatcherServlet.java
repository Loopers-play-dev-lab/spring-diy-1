package com.diy.app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    private final Map<String, Controller> controllerMapping;

    private final ViewResolver viewResolver = new JspViewResolver();

    public DispatcherServlet(Map<String, Controller> controllerMapping) {
        this.controllerMapping = controllerMapping;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
