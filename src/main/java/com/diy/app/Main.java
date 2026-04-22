package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {

        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();


        // version 3
        /*
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        try {
            tomcat.start();
            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();

            System.out.println("서버 실행");
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }

         */

        // version 2
        /*
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        new Thread(() -> {
            try {
                tomcat.start();
                // while true 대신 await
                //  thread 를 blocking-> 서버가 다른 스택에서 멈춰서 다른 로직 수행 불가
                //  -> 별도 스레드 실행하면 해결되지만, 톰캣 실행 흐름이 다른 스레드로 넘어가 스레드 간 컨텍스트 공유가 어려움
                //  => 톰캣 실행은 메인 스레드에서 하고, await 로 스레드가 종료되지 않게 하는 메서드를 다른 스레드로 넘겨주면 됨 (v3)
                tomcat.getServer().await();
            } catch (
    LifecycleException e) {
                throw new RuntimeException();
            }
        }).start();
        System.out.println("Hello, World!");

         */

        // version 1
        /*
        // 웹 요청을 직접 받는 대신 => Spring Boot 의 Embedded Tomcat 사용 (v2)
        try (final ServerSocket serverSocket = new ServerSocket(8080)) {
            // 서버가 요청을 계속 받을 수 있도록 하기 위해 추가
            while (true) {
                try (final Socket clientSocket = serverSocket.accept()) {
                    final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    final PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    // HTTP 요청 읽기
                    final String request = in.readLine();
                    System.out.println("요청: " + request);

                    // HTTP 응답 전송
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println();
                    out.println("<html><body><h1>Hello, World!</h1></body></html>");
                }
            }
        } catch (final IOException e) {
            System.err.println("error = " + e.getMessage());
        }

         */
    }
}
