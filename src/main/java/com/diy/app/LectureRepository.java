package com.diy.app;

import com.diy.framework.bean.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class LectureRepository {

    private final Map<Long, Lecture> lectures = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public void save(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
    }

    public Lecture findById(Long id) {
        return lectures.get(id);
    }

    public List<Lecture> findAll() {
        return new ArrayList<>(lectures.values());
    }

    public void remove(Lecture lecture) {
        lectures.remove(lecture.getId());
    }

    public Long nextId() {
        return sequence.incrementAndGet();
    }
}
