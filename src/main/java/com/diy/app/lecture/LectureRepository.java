package com.diy.app.lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureRepository {

    private final Map<Long, Lecture> store = new HashMap<>();
    private long nextId = 1L;

    public void save(Lecture lecture) {
        lecture.setId(nextId++);
        store.put(lecture.getId(), lecture);
    }

    public List<Lecture> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Lecture lecture) {
        store.put(lecture.getId(), lecture);
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}
