package com.diy.app;

import com.diy.framework.context.annotation.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LectureRepository {

    public LectureRepository() {
        System.out.println("lectureRepository = " + lectureRepository);
    }

    private final Map<Long, Lecture> lectureRepository = new HashMap<>();

    public void save(final Lecture lecture) {
        final long id = lectureRepository.size();
        lectureRepository.put(id, lecture);
        lecture.setId(id);
    }

    public Collection<Lecture> findAll() {
        return lectureRepository.values();
    }
}
