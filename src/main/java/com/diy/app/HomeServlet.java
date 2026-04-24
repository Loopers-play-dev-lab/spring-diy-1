package com.diy.app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("HomeServlet init");
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("req.getRequestURI()  = " + req.getRequestURI());
        super.service(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet called");
        // super.doGet(req, resp);
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().write("<html><body><h1>Home</h1></body></html>");
    }
}
