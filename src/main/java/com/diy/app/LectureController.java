package com.diy.app;

import com.diy.framework.web.context.annotation.Component;
import com.diy.framework.web.mvc.ModelAndView;

import com.diy.framework.web.context.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class LectureController {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureController(final LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public ModelAndView list(final Map<String, ?> params) {
        return new ModelAndView("lecture-list", Map.of("lectures", lectureRepository.findAll()));
    }

    public ModelAndView create(final Map<String, ?> params) {
        Lecture lecture = Lecture.register((String) params.get("name"), new BigDecimal((String) params.get("price")));
        lectureRepository.save(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView update(final Map<String, ?> params) {
        int id = Integer.parseInt((String) params.get("id"));
        Lecture lecture = lectureRepository.findById(id);
        if (lecture != null) {
            lecture.updateName((String) params.get("name"));
            lecture.updatePrice(new BigDecimal((String) params.get("price")));
        }
        return new ModelAndView("redirect:/lectures");
    }

    public ModelAndView delete(final Map<String, ?> params) {
        int id = Integer.parseInt((String) params.get("id"));
        lectureRepository.deleteById(id);
        return new ModelAndView("redirect:/lectures");
    }
}
