package com.diy.app.service;

import com.diy.app.domain.Lecture;
import com.diy.framework.web.LectureRepository;
import com.diy.framework.web.beans.factory.annotation.Autowired;
import com.diy.framework.web.beans.factory.annotation.Component;

import java.util.Collection;

@Component
public class LectureService {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(final LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public void registerLecture(final Lecture lecture) {
        lectureRepository.save(lecture);
    }

    public Collection<Lecture> getLecture() {
        return lectureRepository.findAll();
    }

}
