package com.diy.app.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LectureRequestMapper {
    public static LectureRequest jsonToLectureRequest(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, LectureRequest.class);
    }
}
