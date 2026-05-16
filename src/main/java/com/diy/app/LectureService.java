package com.diy.app;

import com.diy.framework.web.context.annotation.Autowired;
import com.diy.framework.web.context.annotation.Component;

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

    public Lecture findById(final int id) {
        return lectureRepository.findById(id);
    }

    public Collection<Lecture> getLectures() {
        return lectureRepository.findAll();
    }

    public void deleteById(final int id) {
        lectureRepository.deleteById(id);
    }
}
