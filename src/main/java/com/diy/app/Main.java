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
            // [while (true) 없이 실행하면 서버가 죽는다. why?]

            // tomcat.start();가 호출되면 내부적으로 Tomcat이 별도의 스레드들을 띄움 (포트 8080에서 요청을 대기하는 스레드, 요청이 오면 처리할 워커 스레드들 등등)
            // 이 스레드들은 메인 스레드와 별개로 살아있음
            // 즉, 이때 tomcat.start();는 시작만 시키고 바로 리턴 함. 시작시키고 계속 대기가 아닌, 시작시키고 자기 할 일 끝났다고 리턴
            // 그래서 다음 줄로 넘어가면 바로 main() 메서드가 끝남

            // 근데 자바에는 모든 non-daemon 스레드가 끝나면 JVM 프로세스가 종료되는 규칙이 있음
            // 스레드는 두 종류가 있는데,
            // - user thread: 살아있는 한 JVM 안 죽음. 기본값
            // - daemon thread: 데몬 스레드만 살아있으면 JVM 죽어버림

            // main() 메서드가 끝난 순간, JVM은 살아있는 non-daemon 스레드가 있나?를 판단함
            // 이 시점에 tomcat이 띄운 스레드들은 물리적으로는 살아있음 -> 독립적으로 실행되는 별도의 실행 흐름으로 존재함

            // 근데 임베디는 tomcat만 start() 했을 뿐인 상태에선, JVM을 확실하게 살려둘 non-daemon 스레드가 보장되지 않음
            // -> 결과적으로 JVM이 끝내도 되겠다고 판단하고 프로세스 종료됨
            //  tomcat 스레드들도 프로세스가 죽으면서 같이 강제로 죽음

            // 그래서 while (true) {} 로 메인 스레드를 붙잡아두면
            // 메인 스레드는 non-daemon이니까 JVM 안 죽음 -> 서버 유지됨
            while (true) {
                // JVM 살려두는, 임시방편의 방법
            }
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}
