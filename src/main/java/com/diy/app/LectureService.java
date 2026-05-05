package com.diy.app;

import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;

import java.util.Collection;

@Component
public class LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(final LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Lecture save(final Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public Collection<Lecture> findAll() {
        return lectureRepository.findAll();
    }
}