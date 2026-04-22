package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/lectures/*")
public class LectureServlet extends HttpServlet {

    private static final ObjectMapper mapper = new ObjectMapper();
    private LectureService lectureService;

    @Override
    public void init() {
        this.lectureService = new LectureService(new LectureRepository());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("lectures", lectureService.findAll());
        request.getRequestDispatcher("/lecture-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lecture lec = mapper.readValue(request.getInputStream(), Lecture.class);
        lectureService.register(lec);
        response.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getPathInfo().substring(1));
        Lecture patch = mapper.readValue(request.getInputStream(), Lecture.class);
        if (lectureService.update(id, patch)) {
            response.setStatus(200);
        } else {
            response.setStatus(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getPathInfo().substring(1));
        if (lectureService.delete(id)) {
            response.setStatus(204);
        } else {
            response.setStatus(404);
        }
    }
}
