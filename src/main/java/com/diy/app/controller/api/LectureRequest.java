package com.diy.app.controller.api;

import com.diy.app.store.Lecture;

public class LectureRequest {

  public record Create(
      String name,
      int price
  ) {
  }
}
