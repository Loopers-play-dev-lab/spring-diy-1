package com.diy.app.lecture;

import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

import java.util.Collection;

@Component
public class LectureService {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Collection<Lecture> getLectures() {
        return lectureRepository.findAll();
    }

    public void insert(Lecture lecture) {
        lectureRepository.insert(lecture);
    }

    public void update(Lecture lecture) {
        lectureRepository.update(lecture);
    }

    public void delete(Long id) {
        lectureRepository.delete(id);
    }


}
