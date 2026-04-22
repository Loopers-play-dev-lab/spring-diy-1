package com.diy.app.interfaces.api;

import com.diy.app.application.Lecture.LectureService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/lectures")
public class LectureV1Controller extends HttpServlet {
    private final LectureService lectureService = new LectureService();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("Lecture servlet has been initialized");
        super.init(config);
    }

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("lecture-list.jsp");
        req.setAttribute("lectures", lectureService.getLectures());
        dispatcher.forward(req, res);
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = mapper.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});
            lectureService.createLecture(body.get("name"), Long.parseLong(body.get("price")));
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void doPut(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = mapper.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});
            lectureService.updateLecture(Long.parseLong(body.get("id")), body.get("name"), Long.parseLong(body.get("price")));
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void doDelete(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = mapper.readValue(req.getInputStream(), new TypeReference<Map<String, String>>() {});
            lectureService.deleteLecture(Long.parseLong(body.get("id")));
        } catch (Exception e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
