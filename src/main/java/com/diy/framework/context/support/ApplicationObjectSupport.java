package com.diy.framework.context.support;

import com.diy.framework.context.ApplicationContext;

public abstract class ApplicationObjectSupport {

    protected abstract void initApplicationContext(ApplicationContext context);

    public final void setApplicationContext(ApplicationContext context) {
        initApplicationContext(context);
    }
}
