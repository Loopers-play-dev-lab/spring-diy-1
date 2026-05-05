package com.diy.framework.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 실행 중에도 리플렉션으로 읽을 수 있게 설정
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodOrder {

    // @MethodOrder(1) 처럼 순서 값을 넣기 위한 값
    int value();
}