package com.diy.app.repository;

import com.diy.app.Lecture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepositoryImpl implements ILectureRepository {

    public static Map<Long, Lecture> lectures = new HashMap<>();
    public static AtomicLong lectureId = new AtomicLong(0L);

    @Override
    public List<Lecture> findAll() {
        return lectures.values().stream().toList();
    }

    @Override
    public Lecture findById(Long id) {
        return lectures.get(id);
    }

    @Override
    public void save(Lecture lecture) {
        lecture.setId(lectureId.incrementAndGet());
        lectures.put(lecture.getId(), lecture);
    }

    @Override
    public void delete(Lecture lecture) {
        lectures.remove(lecture.getId());
    }

    @Override
    public void update(Lecture lecture) {
        Lecture oldLecture = lectures.get(lecture.getId());
        oldLecture.setName(lecture.getName());
        oldLecture.setPrice(lecture.getPrice());
    }
}
