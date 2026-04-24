package com.diy.app.controller;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet("/")
public class MainController extends HttpServlet {

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("Register Lecture Controller");
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("lecture-registration.jsp");
        dispatcher.forward(request, response);
    }
}
