package com.diy.config;

import com.diy.app.domain.LectureRepository;
import com.diy.app.domain.LectureService;
import com.diy.app.domain.LectureServiceImpl;
import com.diy.app.infrastructure.LectureInmemoryRepository;
import com.diy.app.presentation.LectureController;
import com.diy.module.database.InMemoryDatabase;

public class AppConfig {
    public static LectureController lectureController() {
        return LectureController.getInstance(lectureService());
    }

    public static LectureService lectureService() {
        return LectureServiceImpl.getInstance(lectureRepository());
    }

    private static LectureRepository lectureRepository() {
        return LectureInmemoryRepository.getInstance(inMemoryDatabase());
    }

    private static InMemoryDatabase inMemoryDatabase() {
        return InMemoryDatabase.getInstance();
    }

}
