package com.diy.app.lecture;

import java.util.List;

public class LectureService {

    private final LectureRepository lectureRepository = new LectureRepository();

    public void save(Lecture lecture) {
        lectureRepository.save(lecture);
    }

    public List<Lecture> getLectures() {
        return lectureRepository.findAll();
    }

    public void update(Lecture lecture) {
        lectureRepository.update(lecture);
    }

    public void delete(Lecture lecture) {
        lectureRepository.delete(lecture);
    }
}
