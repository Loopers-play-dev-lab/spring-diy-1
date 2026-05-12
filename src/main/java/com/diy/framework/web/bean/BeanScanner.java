package com.diy.framework.web.bean;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

public class BeanScanner {

	private final Reflections reflections;

	// 내부에서 null 체크가 없는 경우라면, ... ( 가변인자 ) 를 받는 것을 선호하지 않는다.
	public BeanScanner(final String... basePackages){
		reflections = new Reflections((Object)basePackages);
	}

	public Set<Class<?>> scanClassesTypeAnnotationWith(final Class<? extends Annotation> annotation){
		System.out.println("### reflections : " +  reflections.getTypesAnnotatedWith(annotation));
		return reflections.getTypesAnnotatedWith(annotation)
			.stream()
			.filter(type -> (!type.isAnnotation() && !type.isInterface()))
			.collect(Collectors.toSet());
	}

}
