package com.diy.framework.web.bean.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.diy.framework.web.bean.BeanScanner;
import com.diy.framework.web.component.Autowired;
import com.diy.framework.web.component.Component;

public class ApplicationContext {

	private final String basePackage;
	private final List<Object> beans = new ArrayList<Object>();
	private Set<Class<?>> beanClasses = new HashSet<>();

	public ApplicationContext(final String basePackage) {
		this.basePackage = basePackage;
	}

	public void init() {
		final BeanScanner beanScanner = new BeanScanner(basePackage);
		beanClasses.addAll(beanScanner.scanClassesTypeAnnotationWith(Component.class));

		beanClasses.forEach(beanClass -> {
			if ( isBeanInitialized(beanClass)){
				return ;
			}

			final Object bean = createInstance(beanClass);
			saveBean(bean);
		});
	}

	private boolean isBeanInitialized(final Class<?> parameterType) {
		return beans.stream().anyMatch(bean -> bean.getClass().equals(parameterType));
	}

	private void saveBean(final Object bean) {
		beans.add(bean);
	}

	private Object createInstance(Class<?> beanClass) {
		Constructor<?> constructor = resolveConstructor(beanClass);

		try{
			constructor.setAccessible(true);
			try {
				final Object[] parameters = getConstructorParameters(constructor);
				return constructor.newInstance(parameters);
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}

		}finally {
			constructor.setAccessible(false);
		}
	}

	private Object[] getConstructorParameters(final Constructor<?> constructor) {
		final List<Class<?>> parameterTypes = Arrays.stream(constructor.getParameterTypes()).toList();

		if (!beanClasses.containsAll(parameterTypes)) {
			throw new RuntimeException("parameter is not bean");
		}

		return parameterTypes.stream().map(parameterType -> {
			if (isBeanInitialized(parameterType)) {
				return beans.stream().findFirst().get();
			}

			final Object bean = createInstance(parameterType);
			saveBean(bean);

			return bean;
		}).toArray();
	}

	private static Constructor<?> resolveConstructor(Class<?> beanClass) {
		final Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
		Constructor<?> constructor = null;

		final List<Constructor<?>> autowiredConstructors =
			Arrays.stream(constructors).filter(c -> c.isAnnotationPresent(Autowired.class)).toList();

		if ( autowiredConstructors.isEmpty()){
			constructor = constructors[0];
		}

		return constructor;
	}

}
