package com.diy.app.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LectureRepository {

    private final Map<String, Lecture> lectures = new ConcurrentHashMap<>();

    public Lecture save(Lecture lecture) {
        lecture.setId(UUID.randomUUID().toString());
        lectures.put(lecture.getId(), lecture);
        return lecture;
    }

    public List<Lecture> findAll() {
        return new ArrayList<>(lectures.values());
    }

    public void update(Lecture lecture) {
        Lecture existingLecture = lectures.get(lecture.getId());
        if (Objects.isNull(existingLecture)) {
            throw new IllegalArgumentException("Lecture with id " + lecture.getId() + " does not exist");
        }
        lectures.replace(lecture.getId(), lecture);
    }

    public void delete(Lecture lecture) {
        lectures.remove(lecture.getId());
    }
}
