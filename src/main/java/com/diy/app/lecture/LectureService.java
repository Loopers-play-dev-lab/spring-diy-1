package com.diy.app.lecture;

import com.diy.framework.Autowired;
import com.diy.framework.Component;

@Component
public class LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }
}
