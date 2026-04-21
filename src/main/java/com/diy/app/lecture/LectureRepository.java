package com.diy.app.lecture;

import java.util.Map;

public interface LectureRepository {
    void save(Lecture lecture);
    Lecture findById(long id);
    Map<Long, Lecture> findAll();
    void delete(Lecture lecture);
    void update(Lecture lecture);
}
