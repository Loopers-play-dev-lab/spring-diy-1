package com.diy.app;

import com.diy.framework.web.annotations.Controller;
import com.diy.framework.web.annotations.RequestMapping;
import com.diy.framework.web.annotations.RequestMethod;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(methods = RequestMethod.GET)
    public ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doGet called ===");

        Collection<Lecture> lectures = lectureService.getLectures();
        final Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }

    @RequestMapping(methods = RequestMethod.POST)
    public ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPost called ===");

        Lecture lecture = readLecture(req);
        lectureService.register(lecture);

        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.PUT)
    public ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doPut called ===");

        Lecture updated = readLecture(req);
        lectureService.update(updated);

        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.DELETE)
    public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("=== doDelete called ===");

        String id = req.getParameter("id");
        lectureService.delete(id);

        return new ModelAndView("redirect:/lectures");
    }

    private Lecture readLecture(HttpServletRequest req) throws IOException {
        return objectMapper.readValue(req.getInputStream(), Lecture.class);
    }


    //---------------------------------------------------------------------------------------

    private final ObjectMapper objectMapper = new ObjectMapper();
}
