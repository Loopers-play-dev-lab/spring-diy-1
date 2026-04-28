package com.diy.app;

import java.util.*;

public class LectureRepositoryImpl implements LectureRepository {

    private static final Map<Integer, Lecture> map = new LinkedHashMap<>();
    private static int seq = 0;

    @Override
    public Lecture save(Lecture lecture) {
        lecture.setId(seq);
        map.put(seq++, lecture);
        return lecture;
    }

    @Override
    public Lecture update(Lecture lecture) {
        map.put(lecture.getId(), lecture);
        return lecture;
    }

    @Override
    public void delete(Lecture lecture) {
        map.remove(lecture.getId());
    }

    @Override
    public Optional<Lecture> findById(int id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Lecture> findAll() {
        return new ArrayList<>(map.values());
    }
}
