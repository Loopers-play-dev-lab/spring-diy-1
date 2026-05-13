package com.diy.framework.context;

import com.diy.framework.context.annotation.Component;

@Component
public class Foo {

    private final String name;

    public Foo() {
        this.name = "Foo";
    }

    public Foo(String name) {
        this.name = name;
    }

    public String hello() {
        return String.format("%s Hello!", name);
    }
}
