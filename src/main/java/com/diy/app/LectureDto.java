package com.diy.app;

public class LectureDto {
    public record Lecture(Long id, String name) {}

    public static class CreateLecture {

        private Long id;
        private String name;

        public CreateLecture() {
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
