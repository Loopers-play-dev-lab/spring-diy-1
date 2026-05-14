package com.diy.app;

import com.diy.framework.bean.Bean;
import com.diy.framework.bean.Component;
import com.diy.framework.controller.Controller;

@Component
public class ControllerConfiguration {

    private final LectureRepository lectureRepository;

    public ControllerConfiguration(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Bean
    public Controller controller() {
        System.out.println("Bean initialize!!");
        return new LectureController(lectureRepository);
    }
}
