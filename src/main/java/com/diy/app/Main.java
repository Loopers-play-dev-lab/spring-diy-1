package com.diy.app;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class Main {
    public static void main(String[] args) {
        // [임베디드 tomcat]
        // tomcat을 라이브러리처럼 가져와서, 코드로 직접 띄움
        // 따라서 포트 설정, 웹 애플리케이션 컨텍스트 등록, 리소스 위치 알려주기, 실제 start() 호출, JVM 살려두기 이런 역할을 코드가 직접 해줘야 함
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        try {
            tomcat.start();
           // 이대로 실행하면 서버가 죽음
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
