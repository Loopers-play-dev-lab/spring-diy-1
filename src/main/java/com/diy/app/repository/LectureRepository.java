package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import java.util.List;

public interface LectureRepository {
    List<Lecture> find();
    Lecture save(Lecture lecture);
    Lecture updateById(Long id, Lecture updatelecture);
    Lecture deleteById(Long id);
}
