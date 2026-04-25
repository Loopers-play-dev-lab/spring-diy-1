//package com.diy.app.servlet;
//
//import com.diy.app.domain.Lecture;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
//@WebServlet("/lectures")
//public class LectureServlet extends HttpServlet {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    AtomicLong nextId;
//    Map<Long, Lecture> lectureDB;
//
//    @Override
//    public void init() throws ServletException {
//        nextId= new AtomicLong(1L);
//        lectureDB = new ConcurrentHashMap<>();
//        super.init();
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.setAttribute("lectures", lectureDB.values());
//        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
//        Lecture lecture = new Lecture(nextId.getAndAdd(1L), body.getName(), body.getPrice());
//        lectureDB.put(lecture.getId(), lecture);
//
//        resp.sendRedirect("/lectures");
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
//        Lecture lecture = lectureDB.get(body.getId());
//        lecture.update(body);
//
//        resp.setContentType("application/json");
//        resp.getWriter().write(objectMapper.writeValueAsString(body));
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        long id = Long.parseLong(req.getParameter("id"));
//        lectureDB.remove(id);
//
//        resp.setContentType("application/json");
//        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
//}
