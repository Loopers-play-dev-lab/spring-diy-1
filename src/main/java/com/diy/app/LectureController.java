package com.diy.app;

import com.diy.framework.web.annotations.Component;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LectureController implements Controller {

    private final LectureRepository lectureRepository;

    public LectureController(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        } else if ("GET".equals(request.getMethod())) {
            return doGet(request, response);
        }

        throw new RuntimeException("404 Not Found");
    }

    public ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doGet called ===");

        Collection<Lecture> lectures = lectureRepository.findAll();
        final Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }

    public ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPost called ===");

        Lecture lecture = readLecture(req);
        lectureRepository.save(lecture.toLecture());

        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPut called ===");

        Lecture updated = readLecture(req);
        lectureRepository.findById(updated.getId()).update(updated);

        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doDelete called ===");

        String id = req.getParameter("id");
        lectureRepository.deleteById(id);

        return new ModelAndView("redirect:/lectures");
    }

    private Lecture readLecture(HttpServletRequest req) throws IOException {
        return objectMapper.readValue(req.getInputStream(), Lecture.class);
    }


    //---------------------------------------------------------------------------------------

    private final ObjectMapper objectMapper = new ObjectMapper();
}
