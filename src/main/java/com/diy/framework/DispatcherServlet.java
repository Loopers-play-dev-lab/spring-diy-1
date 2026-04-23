package com.diy.framework;

import com.diy.app.lecture.Lecture;
import com.diy.app.lecture.LectureRepository;
import com.diy.app.lecture.LectureService;
import com.diy.app.lecture.request.CreateLectureRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final LectureService lectureService;

    public DispatcherServlet() {
        LectureRepository lectureRepository = new LectureRepository();
        this.lectureService = new LectureService(lectureRepository);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Map<String, ?> params = parseParams(req);
        String requestURI = req.getRequestURI();

        if (requestURI.equals("/lectures")) {
            if (req.getMethod().equals("GET")) {
                handleFindAllLectures(req, resp);
            } else if (req.getMethod().equals("POST")) {
                handleCreateLecture(resp, params);
            }
        }
    }

    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {
            });
        } else {
            return req.getParameterMap();
        }
    }

    private void handleFindAllLectures(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Lecture> lectures = lectureService.getAllLectures();

        req.setAttribute("lectures", lectures);

        String viewPath = "lecture-list.jsp";
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewPath);
        requestDispatcher.forward(req, resp);
    }

    private void handleCreateLecture(HttpServletResponse resp, Map<String, ?> params) {
        String name = (String) params.get("name");
        int price = (int) params.get("price");
        CreateLectureRequest request = new CreateLectureRequest(name, price);

        lectureService.createLecture(request);

        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/lectures");
    }
}
