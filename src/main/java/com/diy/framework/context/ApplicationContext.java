package com.diy.framework.context;

import com.diy.framework.beans.factory.BeanDefinition;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.beans.factory.ClassBeanDefinition;
import com.diy.framework.beans.factory.MethodBeanDefinition;
import com.diy.framework.context.annotation.Bean;
import com.diy.framework.context.annotation.Component;
import com.diy.framework.web.mvc.Controller;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationContext {

    private final String basePackage;
    private final Map<String, BeanDefinition> beanDefinitions = new LinkedHashMap<>();
    private final Map<String, Object> beans = new LinkedHashMap<>();

    public ApplicationContext(final String basePackage) {
        this.basePackage = basePackage;
    }

    // 스프링 컨텍스트 초기화
    public void initialize() {
        final BeanScanner scanner = new BeanScanner(basePackage);

        scanner.scanClassesTypeAnnotatedWith(Component.class).forEach(this::registerClassDefinition);
        scanner.scanClassesTypeAnnotatedWith(com.diy.framework.context.annotation.Controller.class).forEach(this::registerClassDefinition);
        scanner.scanMethodsAnnotatedWith(Bean.class).forEach(this::registerMethodDefinition);

        beanDefinitions.values().forEach(this::resolveBean);
    }

    // 타입으로 빈 조회
    public Object getBean(final Class<?> type) {
        return beanDefinitions.values().stream()
                .filter(definition -> type.isAssignableFrom(definition.getType()))
                .findFirst()
                .map(this::resolveBean)
                .orElseThrow(() -> new RuntimeException("타입에 맞는 빈을 찾을 수 없습니다: " + type.getName()));
    }

    // @Controller 빈을 URL → 핸들러 맵으로 추려서 DispatcherServlet에 공급
    public Map<String, Controller> getControllerMapping() {
        return beans.values().stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(com.diy.framework.context.annotation.Controller.class)) // 자바에서 존재하는 Controller가 있어서 경로다 써줘야하네
                .collect(Collectors.toMap(
                        bean -> bean.getClass().getAnnotation(com.diy.framework.context.annotation.Controller.class).value(),
                        bean -> (Controller) bean
                ));
    }

    // 클래스를 ClassBeanDefinition으로 변환해 정의서 맵에 적재
    private void registerClassDefinition(final Class<?> clazz) {
        final String name = defaultBeanName(clazz.getSimpleName());
        beanDefinitions.putIfAbsent(name, new ClassBeanDefinition(name, clazz));
    }

    // @Bean 메서드를 MethodBeanDefinition으로 변환해 정의서 맵에 적재
    private void registerMethodDefinition(final Method method) {
        final String declared = method.getAnnotation(Bean.class).value();
        final String name = declared.isEmpty() ? method.getName() : declared;
        beanDefinitions.putIfAbsent(name, new MethodBeanDefinition(name, method));
    }

    // 정의서로부터 빈 인스턴스 생성·캐싱 , 싱글톤이네
    private Object resolveBean(final BeanDefinition definition) {
        final Object existing = beans.get(definition.getName());
        if (existing != null) {
            return existing;
        }

        final Object bean = definition.create(this);
        beans.put(definition.getName(), bean);
        return bean;
    }

    // 클래스이름을 빈 이름 컨벤션으로 변환 (첫 글자 소문자)
    private String defaultBeanName(final String simpleName) {
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
