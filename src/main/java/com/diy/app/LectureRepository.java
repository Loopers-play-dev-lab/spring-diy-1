package com.diy.app;

import java.util.Map;

public interface LectureRepository {
    Map<Long, Lecture> findAll();
    void save(Lecture lecture);
    void delete(Lecture lecture);
    void update(Lecture lecture);
}
