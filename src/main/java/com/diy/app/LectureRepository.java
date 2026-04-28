package com.diy.app;

import java.util.List;
import java.util.Optional;

public interface LectureRepository {
    Lecture save(Lecture lecture);
    Lecture update(Lecture lecture);
    void delete(Lecture lecture);
    Optional<Lecture> findById(int id);
    List<Lecture> findAll();
}
