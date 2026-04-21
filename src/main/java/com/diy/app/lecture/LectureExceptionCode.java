package com.diy.app.lecture;

public enum LectureExceptionCode {
    NO_LECTURE_ID("해당 %s id의 강의가 없습니다."),
    ALREADY_ENROLLED("이미 수강 중인 강의입니다."),
    LECTURE_CLOSED("강의가 폐쇄되어 신청할 수 없습니다.");

    private final String messageTemplate;

    LectureExceptionCode(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(messageTemplate, args);
    }
}
