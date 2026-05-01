package com.diy.app.lecture;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {
    private final Map<Long, Lecture> lectureTable = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public LectureRepository() {
    }

    public Collection<Lecture> findAll() {
        return lectureTable.values();
    }

    public void insert(Lecture lecture) {
        Long id = sequence.incrementAndGet();
        lecture.setId(id);
        lectureTable.put(id, lecture);
    }

    public void update(Lecture lecture) {
        lectureTable.replace(lecture.getId(), lecture);
    }

    public void delete(Long id) {
        lectureTable.remove(id);
    }
}
