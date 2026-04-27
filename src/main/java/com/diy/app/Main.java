package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        TomcatWebServer webServer = new TomcatWebServer();
        webServer.start(); //톰캣 생성 후 시작
        // 웨이터에게!!! 8080 포트 앞에 서있어!
    }
}
