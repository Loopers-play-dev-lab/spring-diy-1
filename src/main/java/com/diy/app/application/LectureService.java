package com.diy.app.application;

import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureRepository;
import java.util.List;

public class LectureService {

  private final LectureRepository lectureRepository = new LectureRepository();

  public List<Lecture> getLectures() {
    return lectureRepository.findAll();
  }

  public void register(String name, Long price) {
    lectureRepository.save(name, price);
  }

  public Lecture modify(Long id, String name, Long price) {
    return lectureRepository.update(id, name, price);
  }

  public void delete(Long id) {
    lectureRepository.deleteById(id);
  }
}