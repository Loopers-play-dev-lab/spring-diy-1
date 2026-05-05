package com.diy.app.ctrl;

import com.diy.app.Lecture;
import com.diy.app.repository.ILectureRepository;
import com.diy.app.repository.LectureRepositoryImpl;
import com.diy.app.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class PostController implements Controller {

    private final ILectureRepository repository = new LectureRepositoryImpl();

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        byte[] bytes = req.getInputStream().readAllBytes();
        String body = new String(bytes, StandardCharsets.UTF_8);
        Lecture lecture = new ObjectMapper().readValue(body,Lecture.class);
        repository.save(lecture);
        System.out.println(lecture);
        System.out.println(repository.findAll());
        return new ModelAndView("redirect:/lectures");
    }
}
