package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {

    private final Map<Long, Lecture> store = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Lecture> findByLecture() {
        return new ArrayList<>(store.values());
    }


    public void register(Lecture lecture) {
    }
}
