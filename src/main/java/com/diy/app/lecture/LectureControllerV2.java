package com.diy.app.lecture;

import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;
import com.diy.framework.web.mvc.model.Model;
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
    protected ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Lecture lecture = OBJECT_MAPPER.readValue(req.getInputStream(), Lecture.class);
        lectureService.insert(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(value = "/lectures", methods = {RequestMethod.GET})
    protected ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) {
        Model model = new Model();
        model.addAttribute("lectures", lectureService.getLectures());
        return new ModelAndView("lecture-list", model);
    }
}
