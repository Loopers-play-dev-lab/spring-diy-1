package com.diy.app;

import com.diy.framework.web.bean.context.ApplicationContext;
import com.diy.framework.web.server.TomcatWebServer;

public class LectureApplication {
    public static void main(String[] args) {
		final ApplicationContext ac = new ApplicationContext(LectureApplication.class.getPackageName());
		ac.init();
		
		final TomcatWebServer tomcat = new TomcatWebServer();
        tomcat.start();
    }
}
