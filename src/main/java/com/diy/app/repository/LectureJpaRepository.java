package com.diy.app.repository;

import com.diy.app.domain.Lecture;
import com.diy.framework.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

    Optional<Lecture> findById(Long id);

    Collection<Lecture> findAll();

    Lecture save(Lecture lecture);

    Long delete(Lecture lecture);

}
