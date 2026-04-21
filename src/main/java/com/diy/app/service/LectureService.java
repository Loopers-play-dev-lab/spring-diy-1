package com.diy.app.service;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;

import java.util.Map;

public class LectureService {

    private final LectureRepository lectureRepository = new LectureRepository();

    public Map<String, Lecture> getLectures() {
        return lectureRepository.getLectures();
    }

    public void registerLecture(Lecture lecture) {
        lectureRepository.insertLecture(lecture);
    }

    public String modifyLecture(Lecture lecture) {
        Lecture modifiedLecture = lectureRepository.updateLecture(lecture);
        return modifiedLecture.getId();
    }

    public void deleteLecture(String id) {
        boolean isDeleted = lectureRepository.deleteLecture(id);
        if (!isDeleted) {
           throw new IllegalStateException("Lecture with id " + id + " was deleted.");
        }
    }
}
