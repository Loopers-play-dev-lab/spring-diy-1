package com.diy.app;

import com.diy.framework.web.context.annotation.Autowired;
import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.annotation.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/lectures")
public class LectureController {
    private final LectureService lectureService;

    @Autowired
    public LectureController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(methods = RequestMethod.GET)
    public ModelAndView list() {
        return new ModelAndView("lecture-list", Map.of("lectures", lectureService.getLectures()));
    }

    @RequestMapping(methods = RequestMethod.POST)
    public ModelAndView create(String name, BigDecimal price) {
        Lecture lecture = Lecture.register(name, price);
        lectureService.registerLecture(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.PUT)
    public ModelAndView update(int id, String name, BigDecimal price) {
        Lecture lecture = lectureService.findById(id);
        if (lecture != null) {
            lecture.updateName(name);
            lecture.updatePrice(price);
        }
        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.DELETE)
    public ModelAndView delete(int id) {
        lectureService.deleteById(id);
        return new ModelAndView("redirect:/lectures");
    }
}
