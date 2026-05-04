package com.diy.app.lecture.controller;

import com.diy.app.lecture.LectureRepository;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureListController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("lecture-list", Map.of("lectures", LectureRepository.findAll()));
    }
}
