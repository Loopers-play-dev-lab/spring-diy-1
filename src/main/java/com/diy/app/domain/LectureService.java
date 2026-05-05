package com.diy.app.domain;

import com.diy.app.repository.LecturesRepository;
import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;

@Component
public class LectureService {
    private final LecturesRepository lecturesRepository;

    @Autowired
    public LectureService(LecturesRepository lecturesRepository) {
        this.lecturesRepository = lecturesRepository;
    }
}
