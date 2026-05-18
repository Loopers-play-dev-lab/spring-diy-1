package com.diy.app;

import com.diy.framework.bean.Autowired;
import com.diy.framework.bean.annotation.Controller;
import com.diy.framework.bean.annotation.RequestMapping;
import com.diy.framework.enums.RequestMethod;
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

@Controller
public class LectureControllerV2 {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureControllerV2(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public void decide() {
        System.out.println("RequestMapping is added");
    }

    @RequestMapping(value = "lectures", method = RequestMethod.GET)
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
