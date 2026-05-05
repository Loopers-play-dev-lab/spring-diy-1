package com.diy.app.controller.lecutre;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesMapRepository;
import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.RequestBodyV1;
import com.diy.framework.web.utils.ResponseV1;

public class LecturePostControllerV1 implements ControllerV1 {
    private final LecturesMapRepository lecturesRepository;

    public LecturePostControllerV1() {
        this.lecturesRepository = LecturesMapRepository.getInstance();
    }

    @Override
    public ResponseV1 handle(RequestBodyV1 body) {
        String name = body.get("name").toString();
        int price = Integer.parseInt(body.get("price").toString());

        this.lecturesRepository.save(Lecture.create(name, price));

        return new ResponseV1("success");
    }
}
