package com.diy.app.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {
    private final Map<Long, Lecture> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Lecture save(Lecture lecture) {
        long id = sequence.incrementAndGet();
        lecture.setId(id);
        store.put(id, lecture);
        return lecture;
    }

    public List<Lecture> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Lecture> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<Lecture> update(Lecture lecture) {
        Long id = lecture.getId();
        if (id == null || !store.containsKey(id)) {
            return Optional.empty();
        }
        store.put(id, lecture);
        return Optional.of(lecture);
    }

    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }
}
