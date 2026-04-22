package com.diy.app.application.Lecture;

import com.diy.app.domain.Lecture.Lecture;
import com.diy.app.domain.Lecture.LectureRepository;
import com.diy.app.infrastructure.lecture.LectureRepositoryImpl;

import java.util.Collection;

public class LectureService {
    private final LectureRepository lectureRepository = LectureRepositoryImpl.getInstance();

    public Collection<Lecture> getLectures() {
        return lectureRepository.getLectures();
    }
}
