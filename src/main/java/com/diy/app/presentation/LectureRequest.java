package com.diy.app.presentation;

import com.diy.app.domain.Lecture;

public record LectureRequest(
        String lectureId,
        String name,
        Integer price
) {

    public final Lecture toLecture() {
        return new Lecture(lectureId, name, price);
    }
}
