package com.diy.app;

import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.beans.annotation.Component;

import java.util.Map;

@Component
public class LectureService {
    private final LectureRepository repository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.repository = lectureRepository;
    }

    public Map<Long, Lecture> getLectures()throws Exception{
        return repository.findAll();
    }

    public void addLecture(Lecture lecture)throws Exception{
        repository.save(lecture);
    }

    public void editLecture(Lecture lecture)throws Exception{
        repository.update(lecture);
    }

    public void deleteLecture(Lecture lecture)throws Exception{
        repository.delete(lecture);
    }


}
