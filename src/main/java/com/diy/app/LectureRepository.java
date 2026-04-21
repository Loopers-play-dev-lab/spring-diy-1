package com.diy.app;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {
    private final ConcurrentHashMap<Long, Lecture> lectures = new ConcurrentHashMap<>();
    private final AtomicLong atomicLong = new AtomicLong(0);

    private LectureRepository() {}

    private static class Holder {
        private static final LectureRepository INSTANCE = new LectureRepository();
    }

    public static LectureRepository getInstance() {
        return Holder.INSTANCE;
    }

    public void save(Lecture lecture) {
        if (lecture.getId() != null) {
            lectures.put(lecture.getId(), lecture);
            return;
        }

        Long newId = atomicLong.incrementAndGet();
        // 과제범위가 JPA는 아니라고 생각하기 때문에 리플렉션대신 간단한 setter
        lecture.setId(newId);
        lectures.put(lecture.getId(), lecture);
    }

    public void delete(Long id) {
        lectures.remove(id);
    }

    public Optional<Lecture> findById(Long id) {
        return Optional.ofNullable(lectures.get(id));
    }

    public List<Lecture> findAll() {
        return lectures.values().stream().toList();
    }
}
