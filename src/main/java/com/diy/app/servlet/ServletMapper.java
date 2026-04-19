package com.diy.app.servlet;

import javax.servlet.http.HttpServlet;
import java.util.List;

public class ServletMapper {
    private final List<ServletContext> servletList;
    private final HttpServlet httpServlet;

    public ServletMapper(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
        this.servletList = List.of(new ServletContext("/lectures", "lectureServlet", httpServlet));
    }
    public List<ServletContext> getServletList() {
        return servletList;
    }
}
