package com.diy.app.service;

import com.diy.app.model.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.framework.web.anotation.Autowired;
import com.diy.framework.web.anotation.Component;

import java.util.Collection;

@Component
public class LectureService {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }


    public void register(Lecture lecture) {
        this.lectureRepository.save(lecture);
    }


    public Collection<Lecture> getLectures() {
        return this.lectureRepository.findAll();
    }
}
