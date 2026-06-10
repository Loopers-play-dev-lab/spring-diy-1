package com.diy.app.domain;

import java.util.List;

public interface LectureRepository {
    void add(Lecture request);

    List<Lecture> getLectures();

    Lecture getLecture(String id);

    void modify(Lecture lecture);

    void delete(String lectureId);
}
