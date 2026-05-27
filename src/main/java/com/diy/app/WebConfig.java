package com.diy.app;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;

@Component
public class WebConfig {

    @Bean("/home")
    public Controller homeController() {
        return (req, resp) -> new ModelAndView("redirect:/lectures");
    }
}