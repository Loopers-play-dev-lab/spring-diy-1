package com.diy.framework.web.servlet.handler.mapping;

import com.diy.framework.context.ApplicationContext;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

public class HandlerMappingComposite implements HandlerMapping {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingComposite(ApplicationContext context) {
        this.handlerMappings = List.of(
                new ControllerHandlerMapping(context),
                new AnnotationHandlerMapping(context)
        );
    }

    @Override
    public void initialize() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found HandlerMapping"));
    }
}
