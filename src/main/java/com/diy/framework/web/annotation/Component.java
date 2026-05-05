package com.diy.framework.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 런타임에 bean으로 등록할 클래스를 구분하기 위한 애너테이션
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
