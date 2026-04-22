package com.diy.app.repository;

import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {
    public static final LectureRepository INSTANCE = new LectureRepository();

    private final Map<Long, Lecture> lectures = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong();

    private LectureRepository() {
    }

    public Lecture save(LectureRequest request) {
        long id = idSeq.incrementAndGet();
        Lecture lecture = new Lecture(id, request.getName(), request.getPrice());
        lectures.put(id, lecture);
        return lecture;
    }

    public List<Lecture> findAll() {
        return List.copyOf(lectures.values());
    }

    public Optional<Lecture> findById(Long id) {
        return Optional.ofNullable(lectures.get(id));
    }

    public Optional<Lecture> update(Long id, LectureRequest request) {
        Lecture updated = lectures.computeIfPresent(
                id,
                (key, existing) -> new Lecture(key, request.getName(), request.getPrice())
        );
        return Optional.ofNullable(updated);
    }

    public boolean deleteById(Long id) {
        return lectures.remove(id) != null;
    }
}
