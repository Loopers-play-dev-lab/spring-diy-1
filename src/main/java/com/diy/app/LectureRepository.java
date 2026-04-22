package com.diy.app;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {

    private final Map<Long, Lecture> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public Lecture save(final Lecture lecture) {
        final long id = sequence.getAndIncrement();
        lecture.setId(id);
        store.put(id, lecture);
        return lecture;
    }

    public List<Lecture> findAll() {
        return List.copyOf(store.values());
    }

    public void update(final Lecture lecture) {
        store.put(lecture.getId(), lecture);
    }

    public void delete(final Long id) {
        store.remove(id);
    }
}
