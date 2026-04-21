package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

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

    private String getRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }
}
