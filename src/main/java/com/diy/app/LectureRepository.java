package com.diy.app;

import com.diy.framework.bean.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LectureRepository {
    private final Map<Long, Lecture> lectures = new HashMap<>();

    public void save(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
    }

    public Lecture findById(Long id) {
        return lectures.get(id);
    }

    public List<Lecture> findAll() {
        return new ArrayList<>(lectures.values());
    }

    public void remove(Lecture lecture) {
        lectures.remove(lecture.getId());
    }
}
