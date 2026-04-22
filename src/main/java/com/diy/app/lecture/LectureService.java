package com.diy.app.lecture;

import java.util.Collection;

public class LectureService {

    LectureRepository lectureRepository = new LectureRepository();
    private static long sequence = 0L;


    public Collection<Lecture> getLectures() {
        return lectureRepository.getLectures();
    }

    public void register(String name, int price) {
        Long id = ++sequence;
        lectureRepository.save(new Lecture(id, name, price));
    }

    public void modify(Long id, String name, int price) {
        lectureRepository.update(new Lecture(id, name, price));
    }

    public void delete(Long id) {
        lectureRepository.delete(id);
    }
}

