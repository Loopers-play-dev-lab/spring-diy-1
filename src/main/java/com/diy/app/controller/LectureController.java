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
import java.util.Optional;

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
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(sb.toString(), Map.class);

        String name = map.get("name").toString();
        int price = Integer.parseInt(map.get("price").toString());

        this.lecturesRepository.save(Lecture.create(name, price));
        response.sendRedirect("/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(sb.toString(), Map.class);

        Long id = Long.parseLong(map.get("id").toString());
        this.lecturesRepository.delete(id);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(sb.toString(), Map.class);

        Long id = Long.parseLong(map.get("id").toString());
        String name = map.get("name").toString();
        int price = Integer.parseInt(map.get("price").toString());

        Optional<Lecture> lecture = this.lecturesRepository.getById(id);
        lecture.ifPresent(lecture1 -> {
            lecture1.put(name, price);
            this.lecturesRepository.save(lecture1);
        });
    }
}
