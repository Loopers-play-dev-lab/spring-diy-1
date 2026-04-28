package com.diy.app.lecture;

public class LectureException extends RuntimeException {
    private final LectureExceptionCode code;

    public LectureException(LectureExceptionCode code, Object... args) {
        // 부모인 RuntimeException에 포맷팅된 메시지 전달
        super(code.getFormattedMessage(args));
        this.code = code;
    }

    public LectureExceptionCode getCode() {
        return code;
    }
}
