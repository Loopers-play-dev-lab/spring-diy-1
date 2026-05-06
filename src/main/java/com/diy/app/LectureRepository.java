package com.diy.app;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class LectureRepository {
    private final Map<Integer, Lecture> lectures = new LinkedHashMap<>();

    public void save(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
    }

    public Lecture findById(int id) {
        return lectures.get(id);
    }

    public Collection<Lecture> findAll() {
        return lectures.values();
    }

    public void deleteById(int id) {
        lectures.remove(id);
    }
}
