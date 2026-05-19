package com.diy.app;

import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

@Component
public class LectureService {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(final LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }
}
