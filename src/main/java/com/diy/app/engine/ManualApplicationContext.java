package com.diy.app.engine;

import com.diy.app.repository.InMemoryLectureRepositoryImpl;
import com.diy.app.repository.LectureRepository;
import com.diy.app.servlet.LectureServlet;
import com.diy.app.servlet.ServletMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManualApplicationContext {
    private final ObjectMapper objectMapper;
    private final LectureRepository lectureRepository;
    private final LectureServlet lectureServlet;
    private final ServletMapper servletMapper;

    private ManualApplicationContext(ObjectMapper objectMapper, LectureRepository lectureRepository) {
        this.objectMapper = objectMapper;
        this.lectureRepository = lectureRepository;
        LectureServlet lectureServlet = new LectureServlet(lectureRepository);
        this.lectureServlet = lectureServlet;
        this.servletMapper = new ServletMapper(lectureServlet);
    }

    public static ManualApplicationContext init(){
        InMemoryLectureRepositoryImpl lectureRepository = new InMemoryLectureRepositoryImpl();
        return new ManualApplicationContext(
                new ObjectMapper(),
                lectureRepository
        );
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public LectureRepository getLectureRepository() {
        return lectureRepository;
    }

    public ServletMapper getServletMapper() {
        return servletMapper;
    }

    public LectureServlet getLectureServlet() {
        return lectureServlet;
    }
}
