package com.diy.app.controller.lecutre;

import com.diy.app.repository.LecturesRepository;
import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.Model;
import com.diy.framework.web.utils.RequestBody;

public class LectureDelControllerV1 implements ControllerV1 {
    private final LecturesRepository lecturesRepository;

    public LectureDelControllerV1() {
        this.lecturesRepository = LecturesRepository.getInstance();
    }

    public Model handle(RequestBody body) {
        long id = Long.parseLong(body.get("id").toString());
        this.lecturesRepository.delete(id);

        return new Model("del-lecture");
    }
}
