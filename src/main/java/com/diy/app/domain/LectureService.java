package com.diy.app.domain;

import com.diy.app.repository.LecturesRepository;
import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

@Component
public class LectureService {
    private final LecturesRepository lecturesRepository;

    @Autowired
    public LectureService(LecturesRepository lecturesRepository) {
        this.lecturesRepository = lecturesRepository;
    }
}
