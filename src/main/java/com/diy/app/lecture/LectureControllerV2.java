package com.diy.app.lecture;

import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LectureControllerV2 {
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final LectureService lectureService;

    @Autowired
    public LectureControllerV2(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "/lectures", methods = {RequestMethod.POST})
    public String doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = OBJECT_MAPPER.readValue(req.getInputStream(), Lecture.class);
        lectureService.insert(lecture);
        return "redirect:/lectures";
    }

    @RequestMapping(value = "/lectures", methods = {RequestMethod.GET})
    public String doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("lectures", lectureService.getLectures());
        return "lecture-list";
    }

    @RequestMapping(value = "/lectures", methods = {RequestMethod.PUT})
    public String doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = OBJECT_MAPPER.readValue(req.getInputStream(), Lecture.class);
        lectureService.update(lecture);
        return "redirect:/lectures";
    }

    @RequestMapping(value = "/lectures", methods = {RequestMethod.DELETE})
    public String doDelete(HttpServletRequest req, HttpServletResponse resp) {
        final Long id = Long.valueOf(req.getParameter("id"));
        lectureService.delete(id);
        return "redirect:/lectures";
    }
}
