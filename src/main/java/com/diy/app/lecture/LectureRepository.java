package com.diy.app.lecture;

import java.util.List;

public interface LectureRepository {
    Lecture save(Lecture lecture);
    Lecture update(Lecture lecture);
    Lecture delete(Lecture lecture);
    List<Lecture> findAll();
}
