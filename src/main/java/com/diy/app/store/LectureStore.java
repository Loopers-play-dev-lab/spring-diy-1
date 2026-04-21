package com.diy.app.store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureStore {

  private static final AtomicLong idGenerator = new AtomicLong(0);
  private static final ConcurrentMap<Long, Lecture> lectureMap = new ConcurrentHashMap<>();

  static {
    lectureMap.put(idGenerator.incrementAndGet(), new Lecture(idGenerator.get(), "강의1", 1000));
    lectureMap.put(idGenerator.incrementAndGet(), new Lecture(idGenerator.get(), "강의2", 2000));
    lectureMap.put(idGenerator.incrementAndGet(), new Lecture(idGenerator.get(), "강의3", 3000));
  }

  public List<Lecture> list() {
    return new ArrayList<>(lectureMap.values());
  }

}
