package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Component;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class BeanScannerTest {

    @Test
    void Component_붙은_클래스_찾기() {
        // com.diy.app 패키지 스캔 준비
        BeanScanner beanScanner = new BeanScanner("com.diy.app");

        // Component 붙은 클래스 찾기
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        // 찾은 클래스 출력
        System.out.println(classes);
    }
}