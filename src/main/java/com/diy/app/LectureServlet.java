//package com.diy.app;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
//@WebServlet("/lectures")
//public class LectureServlet extends HttpServlet {
//    private final LectureRepository repository = new LectureRepositoryImpl();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Map<Long, Lecture> lectures = repository.findAll();
//        req.setAttribute("lectures", lectures.values());
//        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Lecture lecture = parseJsonToLecture(req);
//        repository.save(lecture);
//        resp.sendRedirect("/lectures");
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Lecture lecture = parseJsonToLecture(req);
//        repository.update(lecture);
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Lecture lecture = parseJsonToLecture(req);
//        repository.delete(lecture);
//    }
//
//    private Lecture parseJsonToLecture(HttpServletRequest req) throws IOException {
//        req.setCharacterEncoding("UTF-8");
//        ObjectMapper mapper = new ObjectMapper();
//        return(mapper.readValue(req.getInputStream(), Lecture.class));
//    }
//}
