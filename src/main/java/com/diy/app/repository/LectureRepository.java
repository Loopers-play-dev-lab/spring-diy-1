package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LectureRepository {

    private final Map<String, Lecture> lectures = new ConcurrentHashMap<>();

    public Map<String, Lecture> getLectures() {
        return lectures;
    }

    public void insertLecture(Lecture lecture) {
        String uuid = UUID.randomUUID().toString();
        lecture.setId(uuid);
        lectures.put(uuid, lecture);
    }

    public Lecture updateLecture(Lecture lecture) {
        Lecture updatedLecture = lectures.replace(lecture.getId(), lecture);
        if (updatedLecture == null) {
            throw new IllegalArgumentException("Lecture not found");
        }
        return updatedLecture;
    }

    public boolean deleteLecture(String lectureId) {
        return lectures.remove(lectureId) != null;
    }
}
