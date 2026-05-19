package com.diy.app;

import com.diy.app.controller.LectureController;
import com.diy.app.service.LectureService;
import com.diy.framework.web.anotation.Bean;
import com.diy.framework.web.anotation.Component;

@Component
public class LectureConfig {

    private final LectureService lectureService;

    public LectureConfig(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Bean("/lectures/v1")
    public LectureController lectureController() {
        return new LectureController(lectureService);
    }
}
