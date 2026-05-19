package com.test.fixture;

import com.diy.framework.web.context.annotation.Component;

@Component
public class NoDefaultConstructorBean {
    public NoDefaultConstructorBean(String a) {}
    public NoDefaultConstructorBean(String a, int b) {}
}
