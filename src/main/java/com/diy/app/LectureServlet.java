package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("init called.");
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doGet called ===");

        List<Lecture> lectures = findAll();
        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPost called ===");

        Lecture lecture = readLecture(req);
        save(lecture.toLecture());

        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPut called ===");

        Lecture updated = readLecture(req);
        findById(updated.getId()).update(updated);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doDelete called ===");

        String id = req.getParameter("id");
        deleteById(id);

        resp.sendRedirect("/lectures");
    }

    private Lecture readLecture(HttpServletRequest req) throws IOException {
        return objectMapper.readValue(req.getInputStream(), Lecture.class);
    }


    //---------------------------------------------------------------------------------------


    private final Map<String, Lecture> lectureRepository = new ConcurrentHashMap<>();

    private List<Lecture> findAll() {
        return lectureRepository.values().stream().toList();
    }

    private Lecture findById(String id) {
        return lectureRepository.get(id);
    }

    private void save(Lecture lecture) {
        lectureRepository.put(lecture.getId(), lecture);
    }

    private void deleteById(String id) {
        lectureRepository.remove(id);
    }
}
