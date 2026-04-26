package com.diy.app.lecture;

import java.util.*;

public class LectureRepositoryImpl implements LectureRepository {

    private static final Map<Integer,Lecture> lectureMap = new HashMap<>();
    private static int seq = 1;

    @Override
    public Lecture save(Lecture lecture){
        lecture.setId(seq);
        lectureMap.put(seq++, lecture);
        return lecture;
    }

    @Override
    public Lecture update(Lecture lecture){
        lectureMap.put(lecture.getId(), lecture);
        return lecture;
    }

    @Override
    public Lecture delete(Lecture lecture){
        lectureMap.remove(lecture.getId(), lecture);
        return lecture;
    }

    @Override
    public List<Lecture> findAll() {
        return new ArrayList<>(lectureMap.values());
    }
}
