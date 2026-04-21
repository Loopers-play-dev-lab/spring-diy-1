package com.diy.app.lecture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LectureRepositoryTest {

    private LectureRepository lectureRepository;

    @BeforeEach
    void setUp() {
        lectureRepository = new LectureRepository();
    }

    @Test
    void 강의를_저장한다() {
        // given
        Lecture lecture = new Lecture("Spring", 10000L);

        // when
        Lecture saved = lectureRepository.save(lecture);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Spring");
        assertThat(saved.getPrice()).isEqualTo(10000L);
    }

    @Test
    void 전체_강의를_조회한다() {
        // given
        lectureRepository.save(new Lecture("Spring", 10000L));
        lectureRepository.save(new Lecture("JPA", 20000L));

        // when
        List<Lecture> lectures = lectureRepository.findAll();

        // then
        assertThat(lectures).hasSize(2);
    }

    @Test
    void 강의를_수정한다() {
        // given
        Lecture saved = lectureRepository.save(new Lecture("Spring", 10000L));
        Lecture updatedLecture = new Lecture("Spring Boot", 20000L);
        updatedLecture.setId(saved.getId());

        // when
        lectureRepository.update(updatedLecture);

        // then
        List<Lecture> lectures = lectureRepository.findAll();
        assertThat(lectures).hasSize(1);
        assertThat(lectures.get(0).getName()).isEqualTo("Spring Boot");
        assertThat(lectures.get(0).getPrice()).isEqualTo(20000L);
    }

    @Test
    void 존재하지_않는_강의를_수정하면_예외가_발생한다() {
        // given
        Lecture lecture = new Lecture("Spring", 10000L);
        lecture.setId("non-existent-id");

        // when & then
        assertThatThrownBy(() -> lectureRepository.update(lecture))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 강의를_삭제한다() {
        // given
        Lecture saved = lectureRepository.save(new Lecture("Spring", 10000L));

        // when
        lectureRepository.delete(saved);

        // then
        assertThat(lectureRepository.findAll()).isEmpty();
    }
}