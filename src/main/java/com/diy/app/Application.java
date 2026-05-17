package com.diy.app;

import com.diy.framework.web.annotations.Component;
import com.diy.framework.web.beans.factory.ApplicationContext;

@Component
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext(Application.class.getPackageName());
        context.initialize();
    }
}
