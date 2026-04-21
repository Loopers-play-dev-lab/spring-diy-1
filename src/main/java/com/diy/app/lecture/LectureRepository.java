package com.diy.app.lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureRepository {

    private static final Map<Long, Lecture> store = new HashMap<>();
    private static long sequence = 0L;

    public void insert(Lecture lecture) {
        Lecture saveLecture = new Lecture(sequence++, lecture.getName(), lecture.getPrice());
        store.put(saveLecture.getId(), saveLecture);
    }

    public List<Lecture> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Lecture lecture) {
        if (!store.containsKey(lecture.getId())) {
            throw new IllegalArgumentException(String.format("Lecture not found. id: %d", lecture.getId()));
        }

        store.put(lecture.getId(), lecture);
    }
}
