package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final LectureService lectureService = new LectureService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    //강의 목록 조회
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LectureServlet doGet" + req.getRequestURI());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/lecture-list.jsp");
        Collection<Lecture> lectures = lectureService.getLectures();
        req.setAttribute("lectures", lectures);
        requestDispatcher.forward(req, resp);
    }

    // 강의 등록
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LectureRequest request = objectMapper.readValue(req.getReader(), LectureRequest.class);

        String name = request.getName();

        int price = request.getPrice();

        lectureService.register(name, price);

        resp.sendRedirect(req.getContextPath() + "/lectures");
    }

    //강의 수정
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LectureRequest request = objectMapper.readValue(req.getReader(), LectureRequest.class);

        Long id = request.getId();

        String name = request.getName();

        int price = request.getPrice();

        lectureService.modify(id, name, price);

        resp.sendRedirect(req.getContextPath() + "/lectures");
    }

    //강의 삭제
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idText = req.getParameter("id");

        Long id = Long.parseLong(idText);

        lectureService.delete(id);

        resp.sendRedirect(req.getContextPath() + "/lectures");
    }
}

