package com.diy.app.lecture;

import com.diy.framework.Autowired;
import com.diy.framework.Component;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(final LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    public Lecture create(final String name, final int price, final boolean visible) {
        validateName(name);
        if (lectureRepository.containsName(name.trim())) {
            throw new IllegalStateException("Lecture already exists: " + name);
        }

        Lecture lecture = new Lecture(name.trim(), price, visible);
        lectureRepository.save(lecture);
        return lecture;
    }

    public Lecture update(final String originalName, final String updatedName, final int updatedPrice,
                          final boolean updatedVisible) {
        validateName(updatedName);

        String targetName = originalName;
        if (targetName == null || targetName.isBlank()) {
            targetName = updatedName;
        }

        Lecture current = lectureRepository.findByName(targetName);
        if (current == null) {
            throw new NoSuchElementException("Lecture not found: " + targetName);
        }

        String normalizedName = updatedName.trim();
        boolean renamed = !current.getName().equals(normalizedName);
        if (renamed && lectureRepository.containsName(normalizedName)) {
            throw new IllegalStateException("Lecture already exists: " + normalizedName);
        }

        Lecture updatedLecture = new Lecture(normalizedName, updatedPrice, updatedVisible);
        lectureRepository.update(targetName, updatedLecture);
        return updatedLecture;
    }

    public void delete(final String name) {
        validateName(name);

        if (!lectureRepository.deleteByName(name.trim())) {
            throw new NoSuchElementException("Lecture not found: " + name);
        }
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Lecture name must not be blank");
        }
    }
}
