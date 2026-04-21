package com.diy.app.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureDao {

    private final Map<Long, Lecture> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Lecture save(Lecture lecture) {
        final long id = idGenerator.incrementAndGet();
        final Lecture persisted = new Lecture(id, lecture.getName(), lecture.getPrice());
        store.put(id, persisted);
        return persisted;
    }

    public List<Lecture> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Lecture lecture) {
        final Lecture existing = store.get(lecture.getId());
        if (existing == null) {
            throw new IllegalArgumentException("존재하지 않는 강의입니다. id=" + lecture.getId());
        }
        existing.update(lecture.getName(), lecture.getPrice());
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}