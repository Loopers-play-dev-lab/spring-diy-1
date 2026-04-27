package com.diy.app;

import com.diy.framework.web.HandlerMapping;
import com.diy.framework.web.HandlerMappingConfigurer;

public class LectureHandlerMappingConfigurer implements HandlerMappingConfigurer {
    @Override
    public void configure(HandlerMapping handlerMapping) {
        handlerMapping.put("/lectures", new LectureController());
    }
}
