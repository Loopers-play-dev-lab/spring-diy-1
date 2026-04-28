package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private static final LectureRepository lectureRepository = new LectureRepositoryImpl();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // 강의 등록
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        lectureRepository.save(lecture);
        resp.sendRedirect("/lectures");
    }

    // 강의 수정
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        lectureRepository.update(lecture);
    }

    // 강의 삭제
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        lectureRepository.delete(lecture);
    }

    // 강의 목록
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("lectures", lectureRepository.findAll());
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }
}
