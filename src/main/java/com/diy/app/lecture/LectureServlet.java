package com.diy.app.lecture;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
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

    private LectureDatabase lectureDatabase;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        lectureDatabase = new LectureDatabase();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("lecture-list.jsp");
        req.setAttribute("lectures", lectureDatabase.findAll());
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> body = new ObjectMapper().readValue(req.getInputStream(), HashMap.class);
        String title = body.get("title");
        String lecturer = body.get("lecturer");
        lectureDatabase.insert(title, lecturer);
        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> body = new ObjectMapper().readValue(req.getInputStream(), HashMap.class);
        Long id = Long.parseLong(body.get("id"));
        String title = body.get("title");
        String lecturer = body.get("lecturer");
        lectureDatabase.update(id, title, lecturer);
        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> body = new ObjectMapper().readValue(req.getInputStream(), HashMap.class);
        Long id = Long.parseLong(body.get("id"));
        lectureDatabase.delete(id);
        resp.sendRedirect("/lectures");
    }
}
