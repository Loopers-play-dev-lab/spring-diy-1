package com.diy.framework.controller;

import java.lang.reflect.Method;

public record HandleMethod(
        RequestKey requestKey,
        Object bean,
        Method method
) {
}
