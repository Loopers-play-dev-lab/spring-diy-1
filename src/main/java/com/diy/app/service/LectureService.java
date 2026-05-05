package com.diy.app.service;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;

import java.util.Map;

@Component
public class LectureService {

    @Autowired
    private LectureRepository lectureRepository;

    public Map<String, Lecture> getLectures() {
        return lectureRepository.getLectures();
    }

    public void registerLecture(Lecture lecture) {
        lectureRepository.insertLecture(lecture);
    }

    public void modifyLecture(Lecture lecture) {
        lectureRepository.updateLecture(lecture);
    }

    public void deleteLecture(String id) {
        boolean isDeleted = lectureRepository.deleteLecture(id);
        if (!isDeleted) {
           throw new IllegalStateException("Lecture with id " + id + " was deleted.");
        }
    }
}
