package com.diy.framework.web.beans.factory;

import java.lang.reflect.InvocationTargetException;

public class BeanCreator {

    public <T> T create(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) clazz.getDeclaredConstructor().newInstance();
    }

}
