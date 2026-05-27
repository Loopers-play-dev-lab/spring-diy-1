package com.diy.app;

import com.diy.framework.web.context.annotation.Bean;
import com.diy.framework.web.context.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.controller.Controller;

@Component
public class LectureConfig {
    @Bean(name = "/lectures/new")
    public Controller lectureFormController() {
        return params -> new ModelAndView("lecture-registration");
    }
}
