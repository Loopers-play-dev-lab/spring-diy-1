package com.diy.app.controller.lecutre;

import com.diy.app.repository.LecturesMapRepository;
import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.RequestBodyV1;
import com.diy.framework.web.utils.ResponseV1;

public class LectureDelControllerV1 implements ControllerV1 {
    private final LecturesMapRepository lecturesRepository;

    public LectureDelControllerV1() {
        this.lecturesRepository = LecturesMapRepository.getInstance();
    }

    public ResponseV1 handle(RequestBodyV1 body) {
        long id = Long.parseLong(body.get("id").toString());
        this.lecturesRepository.delete(id);

        return new ResponseV1("del-lecture");
    }
}
