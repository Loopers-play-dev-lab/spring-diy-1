package com.diy.app.lecture;

import com.diy.framework.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class LectureRepository {

    private static final List<Lecture> LECTURES = new ArrayList<>();

    static {
        LECTURES.add(new Lecture("스프링 DIY", 30000, true));
        LECTURES.add(new Lecture("리액트 입문", 25000));
    }

    public static List<Lecture> findAll() {
        return Collections.unmodifiableList(LECTURES);
    }

    public static void save(Lecture lecture) {
        LECTURES.add(lecture);
    }

    public static boolean containsName(String name) {
        return LECTURES.stream().anyMatch(lecture -> lecture.getName().equals(name));
    }

    public static Lecture findByName(String name) {
        return LECTURES.stream()
                .filter(lecture -> lecture.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static boolean update(String originalName, Lecture updatedLecture) {
        int index = indexOf(originalName);
        if (index < 0) {
            return false;
        }

        LECTURES.set(index, updatedLecture);
        return true;
    }

    public static boolean deleteByName(String name) {
        int index = indexOf(name);
        if (index < 0) {
            return false;
        }

        LECTURES.remove(index);
        return true;
    }

    private static int indexOf(String name) {
        for (int i = 0; i < LECTURES.size(); i++) {
            if (LECTURES.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
