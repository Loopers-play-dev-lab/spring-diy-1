package com.diy.app.controller.v2;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LecturesRepository;
import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.utils.*;

import java.util.Collection;
import java.util.Optional;

@Controller("/lectures")
public class LectureControllerV2 implements ControllerV2 {
    private final LecturesRepository lecturesRepository;

    @Autowired
    public LectureControllerV2() {
        this.lecturesRepository = LecturesRepository.getInstance();
    }

    @Override
    public ResponseV1 handle(String method, RequestBodyV1 body) throws Exception {
        System.out.println("LectureControllerV2 handle");
        switch (method) {
            case "GET" -> {
                return this.doGet(body);
            }
            case "POST" -> {
                return this.doPost(body);
            }
            case "PUT" -> {
                return this.doPut(body);
            }
            case "DELETE" -> {
                return this.doDelete(body);
            }
        };

        throw new RuntimeException("404 Not Found");
    }

    private ResponseV1 doPost(RequestBodyV1 body) throws Exception {

        String name = body.get("name").toString();
        int price = Integer.parseInt(body.get("price").toString());

        this.lecturesRepository.save(Lecture.create(name, price));

        return new ResponseV1("success");
    }

    public ResponseV1 doDelete(RequestBodyV1 body) {
        long id = Long.parseLong(body.get("id").toString());
        this.lecturesRepository.delete(id);

        return new ResponseV1("del-lecture");
    }

    private ResponseV1 doPut(RequestBodyV1 body) {
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

    private ResponseV1 doGet(RequestBodyV1 body) {
        Collection<Lecture> lectures = lecturesRepository.getAll();

        Model model = new Model();
        model.addModel("lectures", lectures);
        return new ResponseV1("lecture-list",model);
    }
}
