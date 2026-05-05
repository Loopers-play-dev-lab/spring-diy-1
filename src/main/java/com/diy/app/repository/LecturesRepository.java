package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import java.util.Collection;
import java.util.Optional;

public interface LecturesRepository {
    public Lecture save(Lecture lecture);
    public Lecture put(long id, Lecture lecture);

    public void delete(long id);

    public Collection<Lecture> getAll();

    public Optional<Lecture> getById(Long id);
}
