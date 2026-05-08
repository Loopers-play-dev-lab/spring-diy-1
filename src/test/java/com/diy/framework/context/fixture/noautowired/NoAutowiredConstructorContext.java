package com.diy.framework.context.fixture.noautowired;

import com.diy.framework.context.annotation.Component;

@Component
public class NoAutowiredConstructorContext {
    public NoAutowiredConstructorContext() {}
    public NoAutowiredConstructorContext(String str) {}
}
