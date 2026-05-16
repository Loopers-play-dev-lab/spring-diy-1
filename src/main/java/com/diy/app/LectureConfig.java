package com.diy.app;

import com.diy.framework.web.context.annotation.Bean;
import com.diy.framework.web.context.annotation.Component;

@Component
public class LectureConfig {
    private final LectureService lectureService;

    public LectureConfig(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Bean(name = "/lectures")
    public LectureController lectureController() {
        return new LectureController(lectureService);
    }
}
