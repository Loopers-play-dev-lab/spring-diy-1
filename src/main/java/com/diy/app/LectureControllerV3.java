package com.diy.app;

import com.diy.framework.context.annotation.Controller;
import com.diy.framework.context.annotation.RestController;
import com.diy.framework.web.mvc.anotation.RequestMapping;
import com.diy.framework.web.mvc.anotation.RequestMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@RestController
public class LectureControllerV3 {

    private final LectureService lectureService;

    public LectureControllerV3(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(value = "/v3/lectures", methods = {RequestMethod.POST})
    private LectureDto.Lecture createLecture(final LectureDto.CreateLecture body) throws IOException {

        return new LectureDto.Lecture(body.getId(), body.getName());
    }

    @RequestMapping(value = "/v3/lectures", methods = {RequestMethod.GET})
    private LectureDto.Lecture getLectures() {
        return new LectureDto.Lecture(1L, "test");
    }
}
