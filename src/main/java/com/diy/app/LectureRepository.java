package com.diy.app;

import com.diy.framework.web.annotation.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LectureRepository {

    // 메모리에 Lecture 데이터를 저장하는 임시 저장소
    private final Map<Long, Lecture> lectures = new HashMap<>();

    public Lecture save(final Lecture lecture) {
        final long id = lectures.size();
        lectures.put(id, lecture);
        lecture.setId(id);
        return lecture;
    }

    public Collection<Lecture> findAll() {
        return lectures.values();
    }
}