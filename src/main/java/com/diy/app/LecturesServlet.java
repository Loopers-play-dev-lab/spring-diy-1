package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/lectures/*")
public class LecturesServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("init called.");
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            throw new RuntimeException("잘못된 경로입니다.");
        }

        var mapper = new ObjectMapper();
        var body = mapper.readValue(req.getInputStream().readAllBytes(), LectureRequest.class);

        var lecture = Lecture.create(body.name(), body.price());
        LectureRepository.getInstance().save(lecture);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            throw new RuntimeException("잘못된 경로입니다.");
        }

        var mapper = new ObjectMapper();
        var body = mapper.readValue(req.getInputStream().readAllBytes(), LectureRequest.class);

        Long lectureId = Long.parseLong(pathInfo.substring(1));
        Lecture lecture = LectureRepository.getInstance().findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        lecture.update(body.name(), body.price());
        LectureRepository.getInstance().save(lecture);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            throw new RuntimeException("잘못된 경로입니다.");
        }

        Long lectureId = Long.parseLong(pathInfo.substring(1));
        LectureRepository.getInstance().delete(lectureId);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            throw new RuntimeException("잘못된 경로입니다.");
        }

        var lectures = LectureRepository.getInstance().findAll();
        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }
}
