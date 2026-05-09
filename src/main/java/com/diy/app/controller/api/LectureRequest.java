package com.diy.app.controller.api;

import com.diy.app.repository.Lecture;

public class LectureRequest {

  public record Create(
      String name,
      int price
  ) {
  }

  public record Update(
      Long id,
      String name,
      int price
  ) {
    public Lecture toLecture() {
      return new Lecture(id, name, price);
    }
  }

  public record Delete(
      Long id
  ) {
  }
}
