package com.diy.app.presentation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/home")
public class HomeServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("HomeServlet init");
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("HomeServlet service");
        resp.getWriter().write("<!DOCTYPE html><html><head><title>Home</title></head><div><a href='/lecture'>regist</a></div><body>");
        resp.getWriter().write("Home");
    }
}