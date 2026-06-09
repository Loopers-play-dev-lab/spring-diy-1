package com.diy.app;

import com.diy.framework.web.mvc.anotation.RequestBody;
import com.diy.framework.web.mvc.anotation.RequestMapping;
import com.diy.framework.web.mvc.anotation.RequestMethod;
import com.diy.framework.web.mvc.anotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class LectureRestController {

    @RequestMapping(value = "/api/lectures", methods = {RequestMethod.GET})
    public List<Lecture> list() {
        final Lecture l1 = new Lecture();
        l1.setId(1L);
        l1.setName("Spring MVC 따라 만들기");
        l1.setPrice(new BigDecimal(10000));

        final Lecture l2 = new Lecture();
        l2.setId(2L);
        l2.setName("Kotlin 기본");
        l2.setPrice(new BigDecimal(20000));

        return List.of(l1, l2);
    }

    @RequestMapping(value = "/api/lectures", methods = {RequestMethod.POST})
    public Lecture create(@RequestBody final Lecture lecture) {
        lecture.setId(100L);
        lecture.validate();
        return lecture;
    }
}
