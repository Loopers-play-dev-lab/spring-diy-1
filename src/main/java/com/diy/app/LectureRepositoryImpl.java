package com.diy.app;

import java.util.HashMap;
import java.util.Map;

public class LectureRepositoryImpl implements LectureRepository {
    private static final Map<Long, Lecture> lectures = new HashMap<>();
    private static long seq = 0;

    @Override
    public Map<Long, Lecture> findAll() {
        return lectures;
    }

    @Override
    public void save(Lecture lecture) {
        seq++;
        lecture.setId(seq);
        lectures.put(seq, lecture);
    }

    @Override
    public void delete(Lecture lecture) {
        lectures.remove(lecture.getId());
    }

    @Override
    public void update(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
    }
}
