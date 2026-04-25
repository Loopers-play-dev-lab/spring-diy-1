package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final Map<Long,Lecture> map = new HashMap<>();
    private static final AtomicLong counter = new AtomicLong(0L);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        switch (method) {
            case "GET": {
                doGetInternal(req, resp);
                break;
            }
            case "POST": {
                doPostInternal(req, resp);
                break;
            }
            case "PUT": {
                break;
            }
            case "DELETE": {
                break;
            }
            default:throw new ServletException("Unknow method: "+method);
        }
    }
    private void doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Lecture> lectures = map.values().stream().toList();

        req.setAttribute("lectures", lectures);

        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }
    private void doPostInternal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes, StandardCharsets.UTF_8);
        Lecture lecture = new ObjectMapper().readValue(body,Lecture.class);
        lecture.setId(counter.incrementAndGet());
        map.put(lecture.getId(),lecture);
        resp.sendRedirect(req.getContextPath()+"/lectures");
    }

}
