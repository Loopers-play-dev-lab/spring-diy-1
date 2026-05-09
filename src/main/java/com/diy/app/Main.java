package com.diy.app;

import com.diy.framework.context.BeanContext;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;
import com.diy.framework.web.utils.ControllerV2;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        BeanContext bc = new BeanContext(Main.class.getPackage().getName());
        bc.init();

        Map<String, ControllerV2> controllerMap = bc.getControllerMap();
        for(String key : controllerMap.keySet()){
            System.out.println(key);
        }
        DispatcherServlet servlet = new DispatcherServlet(bc.getControllerMap());

        TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }
}
