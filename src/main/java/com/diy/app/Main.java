package com.diy.app;

import com.diy.app.lecture.LectureRepository;
import com.diy.app.lecture.LectureServlet;
import com.diy.framework.web.server.TomcatWebServer;

public class Main {
    public static void main(String[] args) {
        LectureRepository repository = new LectureRepository();
        LectureServlet lectureServlet = new LectureServlet(repository);

        TomcatWebServer server = new TomcatWebServer();
        server.prepareContext();
        server.addServlet("/lectures", "lectureServlet", lectureServlet);
        server.startServer();
    }
}
