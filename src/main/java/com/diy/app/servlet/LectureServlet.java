package com.diy.app.servlet;

import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureRequest;
import com.diy.app.service.LectureService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LectureService lectureService = new LectureService();

    // 등록
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        LectureRequest request = objectMapper.readValue(req.getReader(), LectureRequest.class);
        lectureService.register(request);
        resp.sendRedirect("/lectures");
    }

    // 목록
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Lecture> lectures = lectureService.getAll();
        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    // 수정
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("id"));
        LectureRequest request = objectMapper.readValue(req.getReader(), LectureRequest.class);

        Optional<Lecture> updated = lectureService.modify(id, request);
        if (updated.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.sendRedirect("/lectures");
    }

    // 삭제
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));

        boolean deleted = lectureService.remove(id);
        if (!deleted) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.sendRedirect("/lectures");
    }
}
