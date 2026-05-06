package com.diy.app;

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

    public Lecture register(final Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public Collection<Lecture> findAll() {
        return lectureRepository.findAll();
    }
}
