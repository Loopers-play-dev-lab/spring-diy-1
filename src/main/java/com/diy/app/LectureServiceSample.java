package com.diy.app;

import com.diy.framework.web.mvc.Autowired;
import com.diy.framework.web.mvc.Component;

@Component
public class LectureServiceSample {
    private final LectureRepository repository;

    @Autowired
    public LectureServiceSample(LectureRepository repository){
        this.repository = repository;
    }
}
