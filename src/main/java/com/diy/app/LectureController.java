package com.diy.app;

import com.diy.framework.bean.Autowired;
import com.diy.framework.controller.Controller;
import com.diy.framework.enums.HttpMethod;
import com.diy.framework.value.Model;
import com.diy.framework.value.ModelAndView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

public class LectureController implements Controller {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureController(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return switch (HttpMethod.equals(request.getMethod())) {
            case POST -> doPost(request, response);
            case GET -> doGet(request, response);
            default -> throw new RuntimeException("404 Not Found");
        };
    }

    private ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) {
        Collection<Lecture> lectures = lectureRepository.findAll();
        Model model = new Model(Map.of("lectures", lectures));
        return ModelAndView.of("lecture-list", model);
    }

    private ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final byte[] bodyData = request.getInputStream().readAllBytes();
        final String body = new String(bodyData, StandardCharsets.UTF_8);
        Map<String, Object> data = new ObjectMapper().readValue(body, new TypeReference<>() {});

        long id = lectureRepository.nextId();
        String name = data.get("name").toString();
        double price = Double.parseDouble(data.get("price").toString());

        lectureRepository.save(new Lecture(id, name, price));

        return ModelAndView.fromViewName("redirect:/lectures");
    }
}
