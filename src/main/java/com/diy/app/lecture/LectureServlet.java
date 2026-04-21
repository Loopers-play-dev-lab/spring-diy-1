package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// @WebServlet: 이 서블릿이 처리할 URL
@WebServlet("/lectures")
// HttpServlet을 상속받아야 doGet, doPost 등 HTTP 메서드별 메서드를 오버라이드할 수 있음
public class LectureServlet extends HttpServlet {
    LectureRepositoryImpl repo = new LectureRepositoryImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    // 강의 등록
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // req.getInputStream(): 요청 body를 바이트 스트림으로 읽음
        // readValue: JSON 바이트 스트림 → Lecture 객체로 변환
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        repo.save(lecture);
        resp.sendRedirect("/lectures");
    }

    // 강의 수정
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        repo.update(lecture);
    }

    // 강의 삭제
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Lecture lecture = objectMapper.readValue(req.getInputStream(), Lecture.class);
        repo.delete(lecture);
    }

    // 강의 목록 조회
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("lectures", repo.findAll());
        // getRequestDispatcher: 요청을 JSP로 넘김
        // forward: 서버 내부에서 전달 → URL 안 바뀜, req 데이터 유지
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }


}
