package com.diy.app.repository;

import com.diy.app.model.Lecture;
import com.diy.framework.web.anotation.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LectureRepository {
    private Map<Long, Lecture> lectures = new HashMap<>();

    public void save(Lecture lecture) {
        long id = lectures.size() + 1;
        lecture.setId(id);
        lectures.put(id, lecture);
    }

    public Collection<Lecture> findAll() {
        return lectures.values();
    }
}
