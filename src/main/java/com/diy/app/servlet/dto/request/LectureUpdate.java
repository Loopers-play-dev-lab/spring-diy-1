package com.diy.app.servlet.dto.request;

import java.math.BigDecimal;

public record LectureUpdate(
        Long id,
        String name,
        BigDecimal price
) {
}
