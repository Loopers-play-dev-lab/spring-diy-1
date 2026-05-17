package com.diy.app;

import com.diy.framework.web.annotations.Bean;
import com.diy.framework.web.annotations.Configuration;

@Configuration
public class LectureConfig {

    private final LectureService lectureService;

    public LectureConfig(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Bean
    public LectureController lectureController() {
        return new LectureController(lectureService);
    }

}
