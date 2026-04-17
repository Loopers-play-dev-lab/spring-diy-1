package com.diy.app.controller;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@WebServlet("/lectures")
public class LectureController extends HttpServlet {
    private LecturesRepository lecturesRepository = new LecturesRepository();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("Register Lecture Controller");
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("lecture-list.jsp");
        Collection<Lecture> lectures = lecturesRepository.getAll();
        request.setAttribute("lectures", lectures);
        dispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(sb.toString(), Map.class);

        System.out.println(request.getParameterNames());
        String name = map.get("name").toString();
        int price = Integer.parseInt(map.get("price").toString());

        this.lecturesRepository.save(Lecture.create(name, price));
        response.sendRedirect("/lectures");
    }


}
