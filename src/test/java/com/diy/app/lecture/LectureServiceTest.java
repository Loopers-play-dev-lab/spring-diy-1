package com.diy.app.lecture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LectureServiceTest {

    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        lectureService = new LectureService();
    }

    @Test
    void 강의를_등록한다() {
        // given
        Lecture lecture = new Lecture("Spring", 10000L);

        // when
        lectureService.save(lecture);

        // then
        List<Lecture> lectures = lectureService.getLectures();
        assertThat(lectures).hasSize(1);
        assertThat(lectures.get(0).getName()).isEqualTo("Spring");
    }

    @Test
    void 강의_목록을_조회한다() {
        // given
        lectureService.save(new Lecture("Spring", 10000L));
        lectureService.save(new Lecture("JPA", 20000L));

        // when
        List<Lecture> lectures = lectureService.getLectures();

        // then
        assertThat(lectures).hasSize(2);
    }

    @Test
    void 강의를_수정한다() {
        // given
        lectureService.save(new Lecture("Spring", 10000L));
        Lecture saved = lectureService.getLectures().get(0);
        Lecture updatedLecture = new Lecture("Spring Boot", 20000L);
        updatedLecture.setId(saved.getId());

        // when
        lectureService.update(updatedLecture);

        // then
        List<Lecture> lectures = lectureService.getLectures();
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
        assertThatThrownBy(() -> lectureService.update(lecture))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 강의를_삭제한다() {
        // given
        lectureService.save(new Lecture("Spring", 10000L));
        Lecture saved = lectureService.getLectures().get(0);

        // when
        lectureService.delete(saved);

        // then
        assertThat(lectureService.getLectures()).isEmpty();
    }
}