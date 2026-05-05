package com.diy.app;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/lectures")
public class LectureController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        } else if ("GET".equals(request.getMethod())) {
            return doGet(request, response);
        }

        throw new RuntimeException("404 Not Found");
    }

    protected ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doGet called ===");

        Collection<Lecture> lectures = findAll();
        final Map<String, Object> model = new HashMap<>();
        final Object lectureModels = lectures.stream().map(lecture -> Map.of("id", lecture.getId(), "name", lecture.getName(), "price", lecture.getPrice()));
        model.put("lectures", lectureModels);

        return new ModelAndView("lecture-list", model);
    }

    protected ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPost called ===");

        Lecture lecture = readLecture(req);
        save(lecture.toLecture());

        return new ModelAndView("redirect:/lectures");
    }

    protected ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPut called ===");

        Lecture updated = readLecture(req);
        findById(updated.getId()).update(updated);

        return new ModelAndView("redirect:/lectures");
    }

    protected ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doDelete called ===");

        String id = req.getParameter("id");
        deleteById(id);

        return new ModelAndView("redirect:/lectures");
    }

    private Lecture readLecture(HttpServletRequest req) throws IOException {
        return objectMapper.readValue(req.getInputStream(), Lecture.class);
    }


    //---------------------------------------------------------------------------------------

    private final ObjectMapper objectMapper = new ObjectMapper();
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
