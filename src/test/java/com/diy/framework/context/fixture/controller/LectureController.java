package com.diy.framework.context.fixture.controller;

import com.diy.framework.context.annotation.Component;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequestMapping(path = "/lectures")
public class LectureController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
