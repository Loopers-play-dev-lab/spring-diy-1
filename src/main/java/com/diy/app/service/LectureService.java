package com.diy.app.service;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.build.LectureRepositoryImpl;
import com.diy.framework.web.LectureRepository;
import com.diy.framework.web.beans.factory.annotation.Autowired;
import com.diy.framework.web.beans.factory.annotation.Component;

import java.util.Collection;

@Component
public class LectureService {
    private final LectureRepositoryImpl lectureRepository;

    @Autowired
    public LectureService(final LectureRepositoryImpl lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public void registerLecture(final Lecture lecture) {
        lectureRepository.save(lecture);
    }

    public Collection<Lecture> getLecture() {
        return lectureRepository.findAll();
    }

}
