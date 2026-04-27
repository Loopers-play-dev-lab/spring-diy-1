package com.diy.app.controller.lecutre;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesRepository;
import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.Model;
import com.diy.framework.web.utils.RequestBody;

import java.util.Collection;

public class LectureGetControllerV1 implements ControllerV1 {
    private final LecturesRepository lecturesRepository;

    public LectureGetControllerV1() {
        this.lecturesRepository = LecturesRepository.getInstance();
    }

    @Override
    public Model handle(RequestBody body) {
        Collection<Lecture> lectures = lecturesRepository.getAll();

        Model model = new Model("lecture-list");
        model.addModel("lectures", lectures);
        return model;
    }
}
