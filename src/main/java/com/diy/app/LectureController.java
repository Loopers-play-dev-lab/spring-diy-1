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
    public ModelAndView list(final Map<String, ?> params) {
        return new ModelAndView("lecture-list", Map.of("lectures", lectureService.getLectures()));
    }

    @RequestMapping(methods = RequestMethod.POST)
    public ModelAndView create(final Map<String, ?> params) {
        Lecture lecture = Lecture.register((String) params.get("name"), new BigDecimal((String) params.get("price")));
        lectureService.registerLecture(lecture);
        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.PUT)
    public ModelAndView update(final Map<String, ?> params) {
        int id = Integer.parseInt((String) params.get("id"));
        Lecture lecture = lectureService.findById(id);
        if (lecture != null) {
            lecture.updateName((String) params.get("name"));
            lecture.updatePrice(new BigDecimal((String) params.get("price")));
        }
        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.DELETE)
    public ModelAndView delete(final Map<String, ?> params) {
        int id = Integer.parseInt((String) params.get("id"));
        lectureService.deleteById(id);
        return new ModelAndView("redirect:/lectures");
    }
}
