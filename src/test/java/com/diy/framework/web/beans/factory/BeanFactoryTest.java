package com.diy.framework.web.beans.factory;

import com.diy.app.Lecture;
import com.diy.app.LectureService;
import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Set;

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

    @Test
    void 빈_클래스로_객체_생성하고_저장하기() throws Exception {
        // BeanScanner로 Component 붙은 클래스 찾기
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> beanClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        // BeanFactory가 빈 클래스들로 객체 생성 후 저장
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.initialize(beanClasses);

        // 저장된 Lecture 빈 조회
        Object bean = beanFactory.getBean(Lecture.class);

        // 조회한 빈 출력
        System.out.println(bean);
    }

    @Test
    void Autowired_붙은_생성자_찾기() {
        // LectureService 생성자들 가져오기
        Constructor<?>[] constructors = LectureService.class.getDeclaredConstructors();

        // Autowired 붙은 생성자 출력
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                System.out.println(constructor);
            }
        }
    }
    @Test
    void Autowired_생성자로_빈_주입하기() throws Exception {
        // BeanScanner로 Component 붙은 클래스 찾기
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> beanClasses = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        // BeanFactory가 빈 클래스들로 객체 생성 후 저장
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.initialize(beanClasses);

        // LectureService 빈 조회
        LectureService lectureService = (LectureService) beanFactory.getBean(LectureService.class);

        // 주입된 Lecture 확인
        System.out.println(lectureService.getLecture());
    }

}
