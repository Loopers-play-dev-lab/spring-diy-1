package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.app.lecture.request.UpdateLectureRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final LectureRepository lectureRepository;
    private final ObjectMapper objectMapper;

    public LectureServlet() {
        this.lectureRepository = new LectureRepository();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = getRequestBody(req);
        CreateLectureRequest request = objectMapper.readValue(requestBody, CreateLectureRequest.class);

        Lecture lecture = new Lecture(request.name(), request.price());
        lectureRepository.insert(lecture);

        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/lectures");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Lecture> lectures = lectureRepository.findAll();

        req.setAttribute("lectures", lectures);

        String viewPath = "lecture-list.jsp";
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewPath);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestBody = getRequestBody(req);
        UpdateLectureRequest request = objectMapper.readValue(requestBody, UpdateLectureRequest.class);

        Lecture lecture = new Lecture(request.id(), request.name(), request.price());
        lectureRepository.update(lecture);

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));

        lectureRepository.delete(id);

        resp.sendRedirect("/lectures");
    }

    private String getRequestBody(HttpServletRequest req) throws IOException {
        byte[] bytes = req.getInputStream().readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
