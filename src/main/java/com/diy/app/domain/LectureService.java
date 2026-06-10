package com.diy.app.domain;

import com.diy.app.presentation.LectureRequest;
import com.diy.app.presentation.LectureResponse;

import java.util.List;

public interface LectureService {
    void add(LectureRequest request);

    List<Lecture> getLectures();

    Lecture getLecture(String id);

    void modify(LectureRequest request);

    void delete(String lectureId);
}
