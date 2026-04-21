package com.diy.app.lecture;

import java.util.HashMap;
import java.util.Map;

public class LectureRepository {

    private static final Map<Long, Lecture> store = new HashMap<>();
    private static long sequence = 0L;

    public void insert(Lecture lecture) {
        Lecture saveLecture = new Lecture(sequence++, lecture.getName(), lecture.getPrice());
        store.put(saveLecture.getId(), saveLecture);
    }
}
