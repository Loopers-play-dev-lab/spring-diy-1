package com.diy.app;

import com.diy.app.business.repository.LectureRepository;
import com.diy.framework.beans.factory.BeanStorage;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        BeanStorage beanStorage = BeanStorage.getInstance();
        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
