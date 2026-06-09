package com.diy.framework.web.servlet.handler;

import com.diy.framework.beans.factory.BeanFactoryUtils;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.web.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanNameUrlHandlerMapping extends AbstractHandlerMapping {

    private final Map<String, Object> handlerMap = new LinkedHashMap<>();

    @Override
    protected void initApplicationContext(final ApplicationContext context) {
        final Map<String, Controller> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Controller.class);

        matchingBeans.forEach(this::registerHandler);
    }

    private void registerHandler(final String urlPath, final Object handler) {
        final Object mappedHandler = handlerMap.get("urlPath");
        if (mappedHandler != null && mappedHandler != handler) {
            throw new IllegalStateException(
                    "Cannot map " + handler + " to URL path [" + urlPath +
                            "]: There is already " + handler + " mapped.");
        } else {
            handlerMap.put(urlPath, handler);
        }
    }

    @Override
    protected Object getHandlerInternal(final HttpServletRequest req) throws Exception {
        final String urlPath = req.getRequestURI();
        return handlerMap.get(urlPath);
    }
}
