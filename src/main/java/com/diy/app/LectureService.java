package com.diy.app;

import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Component;

import java.util.Collection;

@Component
public class LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(final LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
        System.out.println("lectureService::lectureRepository = " + lectureRepository);
    }


    public void registerLecture(final Lecture lecture) {
        lectureRepository.save(lecture);
    }

    public Collection<Lecture> getLectures() {
        return lectureRepository.findAll();
    }
}
