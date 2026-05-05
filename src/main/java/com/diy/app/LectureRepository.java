package com.diy.app;

import java.util.Collection;

public interface LectureRepository {
    public Lecture findById(Long id);
    public void put(Long id, Lecture lecture);
    public void remove(Long id);
    public void update(Lecture lecture);
    public long size();
    public Collection<Lecture> values();
    public Collection<Lecture> values(int start, int end);
}
