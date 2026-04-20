package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/lecture")
public class LectureServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("init called.");
        super.init(config);
        getServletContext().setAttribute("lectures", new ArrayList<>());
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do get called.");

        List<Map> lectures = (List<Map>) getServletContext().getAttribute("lectures");
        req.setAttribute("lectures", lectures);

        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("do post called.");

        Map lecture = objectMapper.readValue(req.getInputStream(), Map.class);
        lecture.put("id", UUID.randomUUID().toString());
        List<Map> lectures = (List<Map>) getServletContext().getAttribute("lectures");
        lectures.add(lecture);
        resp.sendRedirect("/lecture");
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("do put called.");

        Map updated = objectMapper.readValue(req.getInputStream(), Map.class);
        String id = (String) updated.get("id");

        List<Map> lectures = (List<Map>) getServletContext().getAttribute("lectures");
        for (Map lecture : lectures) {
            if (id.equals(lecture.get("id"))) {
                lecture.put("name", updated.get("name"));
                lecture.put("price", updated.get("price"));
                break;
            }
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        System.out.println("do delete called.");

        String id = req.getParameter("id");

        List<Map> lectures = (List<Map>) getServletContext().getAttribute("lectures");
        lectures.removeIf(lecture -> id.equals(lecture.get("id")));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}

