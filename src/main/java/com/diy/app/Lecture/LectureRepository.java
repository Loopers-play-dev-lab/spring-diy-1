package com.diy.app.Lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {
	private final Map<Long, Lecture> lectures = new ConcurrentHashMap<>();
	private final AtomicLong idSeq = new AtomicLong();

	LectureRepository() {}

	public List<Lecture> findAll() {
		return new ArrayList<>(lectures.values());
	}

	public Optional<Lecture> findById(Long id) {
		return Optional.ofNullable(lectures.get(id));
	}

	public Optional<Lecture> update(Long id, Lecture lecture) {
		Lecture lectureId = lectures.get(id);

		if (lectureId != null) {
			lectureId.setName(lecture.getName());
			lectureId.setPrice(lecture.getPrice());
			return Optional.of(lectureId);
		}
		return Optional.empty();
	}

	public Lecture save(Lecture lecture) {
		Long id = idSeq.incrementAndGet();
		lecture.setId(id);
		lectures.put(id, lecture);
		return lecture;
	}

	public void deleteById(Long id) {
		lectures.remove(id);
	}

}
