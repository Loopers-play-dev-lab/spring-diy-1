package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        final TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();
    }
}
