package com.diy.app.domain;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {

  // Home 에서 사용하기 위해, static 으로 설정
  // static 미설정 시, Lecture 에서 Post 로 등록해도 값이 리스트에 반영되지 않음.
  private static final ConcurrentHashMap<Long, Lecture> LECTURES = new ConcurrentHashMap<>();
  private static final AtomicLong MAX_LECTURE_ID = new AtomicLong(1L);

  // 생성자로 넣으니까, HomeServlet 에서 최초 실행 시 한번 추가되는 현상 발생
  static {
    Long id = MAX_LECTURE_ID.getAndIncrement();
    LECTURES.put(id, new Lecture(id, "Spring", 10000L));
  }

  public List<Lecture> findAll() {
    return LECTURES.values().stream().toList();
  }

  public Lecture save(String name, Long price) {
    Long id = MAX_LECTURE_ID.getAndIncrement();
    Lecture lecture = new Lecture(id, name, price);
    LECTURES.put(id, lecture);
    return lecture;
  }

  public Lecture update(Long id, String name, Long price) {
    if (!LECTURES.containsKey(id)) {
      throw new NoSuchElementException("Lecture not found: id=" + id);
    }
    Lecture lecture = new Lecture(id, name, price);
    LECTURES.put(id, lecture);
    return lecture;
  }

  public void deleteById(Long id) {
    if (LECTURES.remove(id) == null) {
      throw new NoSuchElementException("Lecture not found: id=" + id);
    }
  }
}