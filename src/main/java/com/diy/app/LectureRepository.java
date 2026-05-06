package com.diy.app;

import java.util.Collection;

public interface LectureRepository {

    Lecture save(Lecture lecture);

    Collection<Lecture> findAll();
}
