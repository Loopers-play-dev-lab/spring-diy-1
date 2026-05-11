package com.diy.app.repository;

import com.diy.app.Entity.Lecture;

import java.util.List;

public interface ILectureRepository {

    public List<Lecture> findAll();

    public Lecture findById(Long id);

    public void save(Lecture lecture);

    public void delete(Lecture lecture);

    public void update(Lecture lecture);
}
