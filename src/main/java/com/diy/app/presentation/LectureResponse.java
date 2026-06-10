package com.diy.app.presentation;

import com.diy.app.domain.Lecture;

public record LectureResponse (
        String lectureId,
        String name,
        Integer price
) {
    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(lecture.getLectureId(), lecture.getName(), lecture.getPrice());
    }
}
