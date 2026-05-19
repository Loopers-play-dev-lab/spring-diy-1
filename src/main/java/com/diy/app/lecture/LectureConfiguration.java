package com.diy.app.lecture;

import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;

@Component
public class LectureConfiguration {

    @Bean("/home")
    public HomeController homeController() {
        return new HomeController();
    }
}
