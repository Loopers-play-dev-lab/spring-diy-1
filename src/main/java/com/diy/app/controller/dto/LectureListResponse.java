package com.diy.app.controller.dto;

import java.util.List;

public record LectureListResponse(
        List<LectureListDto> lectureList
) {
    public record LectureListDto(
            Long id,
            String name,
            Long price
    ) {}
}
