package com.diy.app.controller.lecutre;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesRepository;
import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.Model;
import com.diy.framework.web.utils.RequestBody;

import java.util.Optional;

public class LecturePutControllerV1 implements ControllerV1 {
    private final LecturesRepository lecturesRepository;

    public LecturePutControllerV1() {
        this.lecturesRepository = LecturesRepository.getInstance();
    }

    @Override
    public Model handle(RequestBody body) {
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

        return new Model("put-lecture");
    }
}
