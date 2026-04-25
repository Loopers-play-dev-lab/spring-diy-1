package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.fasterxml.jackson.databind.ObjectMapper;

public record LectureRequest(
        String lectureId,
        String name,
        Integer price
) {
    public static LectureRequest from(Object lecture) {
        return  new ObjectMapper().convertValue(lecture, LectureRequest.class);
    }

    public final Lecture toLecture() {
        return new Lecture(lectureId, name, price);
    }
}
