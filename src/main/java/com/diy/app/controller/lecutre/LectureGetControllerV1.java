package com.diy.app.controller.lecutre;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesMapRepository;
import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.Model;
import com.diy.framework.web.utils.RequestBodyV1;
import com.diy.framework.web.utils.ResponseV1;

import java.util.Collection;

public class LectureGetControllerV1 implements ControllerV1 {
    private final LecturesMapRepository lecturesRepository;

    public LectureGetControllerV1() {
        this.lecturesRepository = LecturesMapRepository.getInstance();
    }

    @Override
    public ResponseV1 handle(RequestBodyV1 body) {
        Collection<Lecture> lectures = lecturesRepository.getAll();

        Model model = new Model();
        model.addModel("lectures", lectures);
        return new ResponseV1("lecture-list",model);
    }
}
