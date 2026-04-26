package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        final TomcatWebServer server = new TomcatWebServer();

        try {
            server.start();
            System.out.println("서버 실행");

        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }






    }
}
