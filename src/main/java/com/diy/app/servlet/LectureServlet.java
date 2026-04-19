package com.diy.app.servlet;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.app.servlet.dto.request.LectureDelete;
import com.diy.app.servlet.dto.request.LecturePost;
import com.diy.app.servlet.dto.request.LectureUpdate;
import com.diy.app.servlet.dto.response.LectureResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class LectureServlet extends HttpServlet {
    private final LectureRepository lectureRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LectureServlet(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final RequestDispatcher dispatcher = req.getRequestDispatcher("lecture-list.jsp");
        List<LectureResponse> lectures = lectureRepository.find().stream().map(LectureResponse::from).toList();
        req.setAttribute("lectures", lectures);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder builder = new StringBuilder();
        while(reader.ready()){
            builder.append(reader.readLine());
        }

        String messageBody = builder.toString();
        LecturePost postRequest = objectMapper.readValue(messageBody, LecturePost.class);
        Lecture lecture = Lecture.makeForSave(postRequest);
        lectureRepository.save(lecture);
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder builder = new StringBuilder();
        while(reader.ready()){
            builder.append(reader.readLine());
        }

        String messageBody = builder.toString();
        LectureUpdate updateRequest = objectMapper.readValue(messageBody, LectureUpdate.class);
        Lecture updateLecture = new Lecture(updateRequest.id(), updateRequest.name(), updateRequest.price());
        lectureRepository.updateById(updateRequest.id(), updateLecture);
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        StringBuilder builder = new StringBuilder();
        while(reader.ready()){
            builder.append(reader.readLine());
        }

        String messageBody = builder.toString();
        LectureDelete updateRequest = objectMapper.readValue(messageBody, LectureDelete.class);
        lectureRepository.deleteById(updateRequest.id());
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/lectures");
    }
}
