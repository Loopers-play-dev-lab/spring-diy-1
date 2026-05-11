package com.diy.app.ctrl;

import com.diy.app.Entity.Lecture;
import com.diy.app.annotation.*;
import com.diy.app.annotation.Controller;
import com.diy.app.repository.ILectureRepository;
import com.diy.app.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping(value = "/lectures")
public class LectureController {
    @Autowired
    private ILectureRepository repository;

    @GetMapping
    public ModelAndView getLecture(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Lecture> lectures = repository.findAll();
        ModelAndView mav = new ModelAndView("lecture-list");
        mav.addObject("lectures", lectures);
        return mav;
    }

    @PostMapping("/")
    public ModelAndView postLecture(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes, StandardCharsets.UTF_8);
        Lecture lecture = new ObjectMapper().readValue(body,Lecture.class);
        repository.save(lecture);
        return new ModelAndView("redirect:/lectures");
    }
    @PutMapping
    public ModelAndView putLecture(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes, StandardCharsets.UTF_8);
        Lecture lecture = new ObjectMapper().readValue(body,Lecture.class);
        repository.update(lecture);
        return new ModelAndView("redirect:/lectures");
    }
    @DelMapping
    public ModelAndView delLecture(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes, StandardCharsets.UTF_8);
        Lecture lecture = new ObjectMapper().readValue(body,Lecture.class);
        repository.delete(lecture);
        return new ModelAndView("redirect:/lectures");
    }

}
