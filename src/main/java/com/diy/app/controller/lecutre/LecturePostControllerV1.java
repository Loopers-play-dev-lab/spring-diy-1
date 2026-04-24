package com.diy.app.controller.lecutre;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesRepository;
import com.diy.framework.web.ControllerV1;
import com.diy.framework.web.Model;
import com.diy.framework.web.RequestBody;

public class LecturePostControllerV1 implements ControllerV1 {
    private final LecturesRepository lecturesRepository;

    public LecturePostControllerV1() {
        this.lecturesRepository = LecturesRepository.getInstance();
    }

    @Override
    public Model handle(RequestBody body) {
        String name = body.get("name").toString();
        int price = Integer.parseInt(body.get("price").toString());

        this.lecturesRepository.save(Lecture.create(name, price));

        return new Model("success");
    }
}
