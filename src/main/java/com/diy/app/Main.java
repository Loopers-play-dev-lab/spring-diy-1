package com.diy.app;

import com.diy.app.engine.TomcatEngine;
import org.apache.catalina.startup.Tomcat;

public class Main {
    public static void main(String[] args) {
        TomcatEngine tomcatEngine = new TomcatEngine(new Tomcat());
        tomcatEngine.startEngine();
    }
}
