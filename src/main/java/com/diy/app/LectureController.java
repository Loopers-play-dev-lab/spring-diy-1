package com.diy.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class LectureController implements Controller{
    private final Map<Long, Lecture> lectureRepository = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return switch (HttpMethod.equals(request.getMethod())) {
            case POST -> doPost(request, response);
            case GET -> doGet(request, response);
            default -> throw new RuntimeException("404 Not Found");
        };
    }

    private ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) {
        Collection<Lecture> lectures = lectureRepository.values();
        Map<String, Object> map = new HashMap<>();
        map.put("lectures", lectures);
        return ModelAndView.of("lecture-list", map);
    }

    private ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonNode body = objectMapper.readTree(request.getInputStream());

        long id = autoIncrement();
        String name = body.get("name").asText();
        double price = body.get("price").asDouble();

        lectureRepository.put(id, new Lecture(id, name, price));

        return ModelAndView.fromViewName("redirect:/lectures");
    }

    private long autoIncrement() {
        return lectureRepository.size() + 1L;
    }
}
