package com.diy.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class LectureRepository {

    private final List<Lecture> lectures = new ArrayList<>();
    private final AtomicInteger idSeq = new AtomicInteger(0);

    public Lecture save(Lecture lecture) {
        lecture.setId(idSeq.incrementAndGet());
        lectures.add(lecture);
        return lecture;
    }

    public List<Lecture> findAll() {
        return lectures;
    }

    public Optional<Lecture> findById(int id) {
        return lectures.stream()
                .filter(lecture -> lecture.getId() == id)
                .findFirst();
    }

    public boolean deleteById(int id) {
        return lectures.removeIf(lecture -> lecture.getId() == id);
    }
}
