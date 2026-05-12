package com.diy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.diy.app.domain.Lecture;

public class ReflectionTest {

	@Test
	@DisplayName("Lecture 생성자 찾기")
	void find_lecture_constructor() throws NoSuchMethodException {
		Class<Lecture> lectureClass = Lecture.class;
		Constructor<Lecture> lectureConstructor = lectureClass.getDeclaredConstructor(Long.class, String.class, Long.class);
		System.out.println(lectureConstructor);
	}

	@Test
	@DisplayName("Lecture 인스턴스 동적 생성")
	void create_lecture_dynamic_instance() throws InstantiationException, IllegalAccessException,
		NoSuchMethodException, InvocationTargetException {
		Class<Lecture> lectureClass = Lecture.class;

		// 클래스를 동적 생성한다는 부분에 대해서, 자세히 이해가 가지 않음.
		Lecture lec = lectureClass.
			getDeclaredConstructor(Long.class, String.class,Long.class).newInstance(1L,"test",3L);
		System.out.println(lec);
	}

	@Test
	@DisplayName("private 메서드 찾기")
	void find_private_method() throws NoSuchMethodException {
		Class<Lecture> lectureClass = Lecture.class;
		System.out.println(Arrays.toString(lectureClass.getDeclaredMethods()));
		System.out.println(lectureClass.getDeclaredMethod("printAll"));
	}

	@Test
	@DisplayName("private 메서드 호출")
	void call_private_method() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class<Lecture> lectureClass = Lecture.class;
		// java.lang.IllegalAccessException: class com.diy.ReflectionTest
		// cannot access a member of class com.diy.app.domain.Lecture with modifiers "private"
		// lectureClass.getDeclaredMethod("printAll").invoke(new Lecture(1L,"test",3L));

		Method m = lectureClass.getDeclaredMethod("printAll");
		m.setAccessible(true);
		m.invoke(new Lecture(1L,"test",3L));
	}

	@Test
	@DisplayName("애너테이션으로 메서드 찾기")
	void find_method_by_annotation() {
		Class<Lecture> lectureClass = Lecture.class;
		for (Method m : lectureClass.getDeclaredMethods()) {
			if (m.isAnnotationPresent(Deprecated.class)) {
				System.out.println(m.getName());
			}
		}
	}

	@Test
	@DisplayName("@MethodOrder 애너테이션 정보 조회")
	void get_method_order_info() {

	}



}
