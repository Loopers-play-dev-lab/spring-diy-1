package com.diy.app.lecture.request;

public record UpdateLectureRequest(
        Long id,
        String name,
        int price
) {
}
