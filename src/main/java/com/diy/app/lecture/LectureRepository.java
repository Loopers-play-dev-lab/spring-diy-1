package com.diy.app.lecture;

import java.util.List;

public interface LectureRepository {

    void insert(Lecture lecture);

    List<Lecture> findAll();

    void update(Lecture lecture);

    void delete(Long id);
}
