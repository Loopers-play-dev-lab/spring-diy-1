package com.diy.framework.exception;

public class MethodNotAllowedException extends RuntimeException {

    public MethodNotAllowedException(String method) {
        super("지원하지 않는 HTTP 메서드입니다: " + method);
    }
}
