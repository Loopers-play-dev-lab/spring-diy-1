package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LecturesRepository {
    long id;
    private static Map<Long, Lecture> store;

    public LecturesRepository() {
        id = 0L;
        store = new HashMap<>();
    }

    public Lecture save(Lecture lecture) {
        lecture.setId(id++);

        return store.put(lecture.getId(), lecture);
    }

    public Lecture put(long id, Lecture lecture) {
        Lecture old = store.get(id);
        if (old == null) {
            throw new RuntimeException("Lecture not found");
        }
        return store.put(id, lecture);
    }

    public void delete(long id) {
        store.remove(id);
    }

    public Collection<Lecture> getAll() {
        return store.values();
    }
}
