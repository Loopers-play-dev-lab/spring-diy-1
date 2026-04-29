package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        TomcatWebServer server = new TomcatWebServer();
        server.start();
    }
}
/*
    1. Controller 인터페이스 사용
    2. 요청이 오면 적절한 컨트롤러를 찾을 수 있도록 매핑하는 기능을 구현하자.
    3. 컨트롤러의 준비가 끝났다면 if-else 로 나눠 놓은 비지니스 로직을 컨트롤러로 옮겨보자.
 */