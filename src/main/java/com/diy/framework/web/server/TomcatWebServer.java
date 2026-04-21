package com.diy.framework.web.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

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
            tomcat.start();
            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("톰켓 서버 실행 중 예외가 발생했습니다.", e);
        }
    }

    // 톰캣의 정식 웹 폴더(docBase)를 src/main/resources 로 지정
    // Default 는 src/main/webapp
    // 정통 Jsp 프로젝트에서는
    // src/main/webapp/static 에 정적파일(css, image, js 등)
    // src/main/webapp/WEB-INF 밑에 Jsp 파일, web.xml 이 존재
    // WEB-INF/views : JSP 파일
    // WEB-INF/classes : .class 파일 (war 로 만들면 생성됨)
    private void setServerContext() {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        // 루트(/)로 접속 시, Path 경로로 연결. 정적자원을 읽기 위한 Path 설정.
        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        ErrorPage error400Page = new ErrorPage();
        error400Page.setErrorCode(400);
        error400Page.setLocation("/error.jsp");

        ErrorPage error500Page = new ErrorPage();
        error500Page.setErrorCode(500);
        error500Page.setLocation("/error.jsp");

        context.addErrorPage(error400Page);
        context.addErrorPage(error500Page);

        setServerResources(context);
    }

    // 톰캣이 웹 소스(.class)를 관리하는 위치(/WEB-INF/classes)를 다른 경로(내 classpath)에 연결 (심볼릭 링크)
    private void setServerResources(final Context context) {
        // 내 컴퓨터의 소스 classpath (compile 후 경로)
        final String classPath = getClassPath();

        final StandardRoot resources = new StandardRoot(context);
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
