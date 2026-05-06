package com.diy.app;

import com.diy.framework.web.beans.factory.annotation.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MemoryLectureRepository implements LectureRepository {

    private final Map<Long, Lecture> store = new HashMap<>();

    @Override
    public Lecture save(final Lecture lecture) {
        final long id = store.size();
        lecture.setId(id);
        store.put(id, lecture);
        return lecture;
    }

    @Override
    public Collection<Lecture> findAll() {
        return store.values();
    }
}
