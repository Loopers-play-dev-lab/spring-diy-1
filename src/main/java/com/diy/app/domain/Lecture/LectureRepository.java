package com.diy.app.domain.Lecture;

import java.util.Collection;

public interface LectureRepository {
    Collection<Lecture> getLectures();
    Lecture save(final Lecture lecture);
    Lecture getLectureById(final long id);
}
