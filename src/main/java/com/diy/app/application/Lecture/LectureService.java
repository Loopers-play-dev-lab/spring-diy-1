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

    public void createLecture(final String name, final long price) {
        lectureRepository.save(new Lecture(name, price));
    }

    public void updateLecture(final long id, final String name, final long price) throws Exception {
        final Lecture lecture = lectureRepository.getLectureById(id);
        if (lecture == null) throw new Exception("lecture not found");

        lecture.setName(name);
        lecture.setPrice(price);
    }
}
