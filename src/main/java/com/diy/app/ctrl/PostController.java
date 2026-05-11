package com.diy.app.ctrl;

import com.diy.app.Entity.Lecture;
import com.diy.app.annotation.Autowired;
import com.diy.app.annotation.Component;
import com.diy.app.repository.ILectureRepository;
import com.diy.app.repository.LectureRepositoryImpl;
import com.diy.app.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class PostController implements Controller {

    private final ILectureRepository repository;

    @Autowired
    PostController(ILectureRepository repository) {
        this.repository = repository;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes, StandardCharsets.UTF_8);
        Lecture lecture = new ObjectMapper().readValue(body,Lecture.class);
        repository.save(lecture);
        return new ModelAndView("redirect:/lectures");
    }
}
