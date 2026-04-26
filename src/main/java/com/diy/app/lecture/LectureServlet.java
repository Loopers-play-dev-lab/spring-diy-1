package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private LectureRepository lectureRepository = new LectureRepository();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("lecture-list.jsp");
        req.setAttribute("lectures", lectureRepository.findAll());
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, Object> body =
                objectMapper.readValue(req.getInputStream(), HashMap.class);

        String name = body.get("name").toString();
        int price = Integer.parseInt(body.get("price").toString());

        lectureRepository.save(name, price);

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, Object> body =
                objectMapper.readValue(req.getInputStream(), HashMap.class);

        Long id = Long.parseLong(body.get("id").toString());
        String name = body.get("name").toString();
        int price = Integer.parseInt(body.get("price").toString());

        lectureRepository.update(id, name, price);

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));

        lectureRepository.delete(id);

        resp.sendRedirect("/lectures");
    }
}
