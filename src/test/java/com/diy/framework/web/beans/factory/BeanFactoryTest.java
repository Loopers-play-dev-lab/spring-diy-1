package com.diy.framework.web.beans.factory;

import com.diy.app.Lecture;
import org.junit.jupiter.api.Test;

public class BeanFactoryTest {

    @Test
    void 빈_저장하고_조회하기() {
        // BeanFactory 생성
        BeanFactory beanFactory = new BeanFactory();

        // 저장할 Lecture 객체 생성
        Lecture lecture = new Lecture();

        // Lecture 빈 저장
        beanFactory.addBean(Lecture.class, lecture);

        // Lecture 빈 조회
        Object bean = beanFactory.getBean(Lecture.class);

        // 조회한 빈 출력
        System.out.println(bean);
    }
}
