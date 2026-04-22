package com.diy.app;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("init called.");
        super.init(config);
    }

    @Override
    public void doGet(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get called.");
    }

    @Override
    public void doPost(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post called.");
    }

    @Override
    public void doPut(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("put called.");
    }

    @Override
    public void doDelete(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("delete called.");
    }
}
