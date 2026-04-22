package com.diy.app.lecture;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LectureRepository {

    private static Map<Long, Lecture> lectures = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    public List<Lecture> getLectures() {
        return new ArrayList<>(lectures.values());
    }

    public void save(Lecture lecture) {
        lecture.setId(++sequence);
        lectures.put(lecture.getId(), lecture);
    }

    public void delete(Long lectureId) {
        lectures.remove(lectureId);
    }

    public void update(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
    }

}
