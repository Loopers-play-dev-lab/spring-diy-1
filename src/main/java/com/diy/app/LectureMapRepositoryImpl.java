package com.diy.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class LectureMapRepositoryImpl implements LectureRepository{
    ConcurrentHashMap<Long, Lecture> lectureMap = new ConcurrentHashMap<>();

    @Override
    public Lecture findById(Long id) {
        return lectureMap.getOrDefault(id, null);
    }

    @Override
    public void put(Long id, Lecture lecture) {
        lectureMap.put(id, lecture);
    }

    @Override
    public void remove(Long id) {
        lectureMap.remove(id);
    }

    @Override
    public void update(Lecture lecture) {
        lectureMap.put(lecture.getId(), lecture);
    }

    @Override
    public long size() {
        return lectureMap.size();
    }

    @Override
    public Collection<Lecture> values() {
        return lectureMap.values();
    }

    @Override
    public Collection<Lecture> values(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("start must be less than end");
        }

        ArrayList<Lecture> lectures = new ArrayList<>(end - start + 1);
        for (int i = 0; i < end - start + 1; i++) {
            lectures.add(lectureMap.get(start + i));
        }
        return lectures;
    }
}
