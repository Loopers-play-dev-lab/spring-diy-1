package com.diy.app.lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureRepository {

    private Map<Long, Lecture> store = new HashMap<>();
    private long seq = 0L;

    public Lecture save(String name, int price) {
        Lecture lecture = new Lecture(name, price);
        lecture.setId(++seq);
        store.put(lecture.getId(), lecture);
        return lecture;
    }

    public List<Lecture> findAll() {
        return new ArrayList<Lecture>(store.values());
    }

    public void update(Long id, String name, int price) {
        Lecture lecture = new Lecture(name, price);
        lecture.setId(id);

        store.put(lecture.getId(), lecture);
    }

    public void delete(Long id) {
        store.remove(id);
    }
}
