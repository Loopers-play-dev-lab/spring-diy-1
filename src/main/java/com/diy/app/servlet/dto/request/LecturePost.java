package com.diy.app.servlet.dto.request;

import java.math.BigDecimal;

public record LecturePost(
        String name,
        BigDecimal price
) {
}
