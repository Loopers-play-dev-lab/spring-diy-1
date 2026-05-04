package com.diy.app.lecture.controller;

import com.diy.app.lecture.service.LectureService;
import com.diy.framework.web.beans.factory.Autowired;
import com.diy.framework.web.beans.factory.Component;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LectureListController implements Controller {

    private final LectureService lectureService;

    @Autowired
    public LectureListController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        return new ModelAndView("lecture-list", Map.of("lectures", lectureService.findAll()));
    }
}
