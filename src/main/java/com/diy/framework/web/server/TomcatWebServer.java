package com.diy.framework.web.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;


public class TomcatWebServer {

    private final Tomcat tomcat = new Tomcat();
    private final int port = 8080;

    public void start() {
        setServerContext();
        startDaemonAwaitThread();
        startServerInternal();
    }

    public void startServerInternal() {
        try {
            tomcat.setPort(port);
            // 스레드에서 start부터 하면 안되나?
            tomcat.start();
            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("톰켓 서버 실행 중 예외가 발생했습니다.", e);
        }
    }

    private void setServerContext() {
        // resource 파일 위치 가져오기
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        // 절대 경로로 변경 (File이라 헷갈렸는데 디렉토리로 찾아지는듯 -> 리눅스는 모든 것이 파일이다라는 마인드가 있어서 적용된 것으로 보임)
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        // url 경로와 리소스 경로 매핑 (contextPath를 /abc로 바꾸면 prefix가 붙게됨)
        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        setServerResources(context);
    }

    private void setServerResources(final Context context) {
        final String classPath = getClassPath();

        // 발드된 리소스 file Path main까지 들어가져있음
        final StandardRoot resources = new StandardRoot(context);
        // 내부적으로 WEB-INF/classes 디렉토리와 마운팅이 되어있다고 나오는데 톰캣에서 저 위치를 외부 통신할때 사용할 수 있도록 되어있는 것으로 예상됨
        // internalPath는 저기중에서도 특정 위치의 애들만 가져올 수 있도록 하위 애들 가져올 수 있게 만들어지는듯
        // /com 이렇게해도 아래 하위에 있는 애들은 전부 들어가져서 크게 문제는 없어보이긴 하는데...
        // 질문: resources라고 build 하위에 있는 애들인데 막상 classPath는 build/classes/java/main까지 들어감 왜 그런거지....
        resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", classPath, "/"));

        context.setResources(resources);
    }

    private String getClassPath() {
        try {
            final CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();

            return new File(codeSource.getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void startDaemonAwaitThread() {
        final Thread awaitThread = new Thread(() -> TomcatWebServer.this.tomcat.getServer().await());
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
