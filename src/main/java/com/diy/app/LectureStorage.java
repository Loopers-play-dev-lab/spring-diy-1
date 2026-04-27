package com.diy.app;

import java.util.ArrayList;
import java.util.List;

public class LectureStorage {
    public static final List<Lecture> LECTURES = new ArrayList<>();

    static {
        LECTURES.add(new Lecture(1L, "스프링 DIY", 30000L));
        LECTURES.add(new Lecture(2L, "리액트 입문", 25000L));
    }
}
