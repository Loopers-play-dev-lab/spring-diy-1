package com.diy.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//요청을 처리하는 공통 스펙을 정의하는 인터페이스
@FunctionalInterface
public interface Controller {
    void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}

