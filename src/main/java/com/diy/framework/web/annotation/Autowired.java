package com.diy.framework.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 의존성 주입에 사용할 생성자를 표시하는 애너테이션
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}