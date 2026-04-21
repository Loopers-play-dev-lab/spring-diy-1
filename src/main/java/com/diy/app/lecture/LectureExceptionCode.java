package com.diy.app.lecture;

public enum LectureExceptionCode {
    NO_LECTURE_ID("해당 %s id의 강의가 없습니다."),
    INVALID_LECTURE_ID("입력받은 %s id가 잘못되었습니다.");

    private final String messageTemplate;

    LectureExceptionCode(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}
