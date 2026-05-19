package com.diy.framework.web.servlet;

import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.servlet.handler.mapping.AnnotationHandlerMapping;
import com.diy.framework.web.servlet.handler.mapping.ControllerHandlerMapping;
import com.diy.framework.web.servlet.handler.mapping.HandlerMapping;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class HandlerMappingComposite {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingComposite(ApplicationContext context) {
        this.handlerMappings = List.of(
                new ControllerHandlerMapping(context),
                new AnnotationHandlerMapping(context)
        );
    }

    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found HandlerMapping"));
    }
}
