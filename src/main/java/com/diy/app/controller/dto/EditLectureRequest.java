package com.diy.app.controller.dto;

public record EditLectureRequest(
        Long id,
        String name,
        Long price
) {
}
