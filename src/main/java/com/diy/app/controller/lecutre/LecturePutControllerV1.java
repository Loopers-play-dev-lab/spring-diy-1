package com.diy.app.controller.lecutre;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesMapRepository;
import com.diy.app.repository.LecturesRepository;
import com.diy.framework.web.beans.annotation.Autowired;
import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.RequestBodyV1;
import com.diy.framework.web.utils.ResponseV1;

import java.util.Optional;

public class LecturePutControllerV1 implements ControllerV1 {
    private final LecturesRepository lecturesRepository;

    @Autowired
    public LecturePutControllerV1() {
        this.lecturesRepository = LecturesMapRepository.getInstance();
    }

    @Override
    public ResponseV1 handle(RequestBodyV1 body) {
        Long id = Long.parseLong(body.get("id").toString());
        String name = body.get("name").toString();
        int price = Integer.parseInt(body.get("price").toString());

        Optional<Lecture> lecture = this.lecturesRepository.getById(id);
        if(lecture.isEmpty()) {
            throw new RuntimeException("Lecture with id " + id + " not found");
        }

        Lecture lecture1 = lecture.get();
        lecture1.put(name, price);
        this.lecturesRepository.save(lecture1);

        return new ResponseV1("put-lecture");
    }
}
