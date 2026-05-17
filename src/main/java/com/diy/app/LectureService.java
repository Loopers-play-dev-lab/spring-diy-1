package com.diy.app;

import com.diy.framework.web.annotations.Component;

import java.util.Collection;

@Component
public class LectureService {

    private final LectureRepository lectureRepository;

    public LectureService(final LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
        System.out.println("lectureService::lectureRepository = " + lectureRepository);
    }

    public void register(final Lecture lecture) {
        lectureRepository.save(lecture.toLecture());
    }

    public Collection<Lecture> getLectures() {
        return lectureRepository.findAll();
    }

    public void update(final Lecture newLecture) {
        lectureRepository.findById(newLecture.getId()).update(newLecture);
    }

    public int delete(final String id) {
        lectureRepository.deleteById(id);
        return 1;
    }
}
