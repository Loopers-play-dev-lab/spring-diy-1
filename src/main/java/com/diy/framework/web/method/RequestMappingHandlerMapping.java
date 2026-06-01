package com.diy.framework.web.method;

import com.diy.framework.beans.factory.BeanFactoryUtils;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.mvc.anotation.RequestMapping;
import com.diy.framework.web.mvc.anotation.RequestMethod;
import com.diy.framework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestMappingHandlerMapping extends AbstractHandlerMapping {

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    @Override
    protected void initApplicationContext(final ApplicationContext context) {
        final Map<String, Object> matchingBeans = BeanFactoryUtils.beansOfAnnotated(context, Controller.class);

        matchingBeans.forEach((key, handler) -> detectHandlerMethods(handler));
    }

    private void detectHandlerMethods(final Object handler) {
        Class<?> clazz = handler.getClass();
        Method[] handlerMethods = clazz.getDeclaredMethods();

        StringBuilder parentPath = new StringBuilder();
        Set<RequestMethod> parentRequestMethods = new HashSet<>();

        boolean classHasRequestMapping = clazz.isAnnotationPresent(RequestMapping.class);

        if (classHasRequestMapping) {
            RequestMapping requestMapping = clazz.getDeclaredAnnotation(RequestMapping.class);

            setRequestMappingInfo(requestMapping, parentPath, parentRequestMethods);
        }

        final String basePath = parentPath.toString();

        final Map<Method, RequestMappingInfo> methods = extractMethodMappings(handlerMethods, basePath, parentRequestMethods);

        methods.forEach((method, mapping) -> registerHandlerMethod(handler, method, mapping));
   	}

    private Map<Method, RequestMappingInfo> extractMethodMappings(final Method[] handlerMethods, final String basePath, final Set<RequestMethod> baseMethods) {
        final Map<Method, RequestMappingInfo> methods = new LinkedHashMap<>();

        Arrays.stream(handlerMethods).forEach(method -> {
            StringBuilder path = new StringBuilder(basePath);
            Set<RequestMethod> requestMethods = new HashSet<>(baseMethods);

            boolean methodHasRequestMapping = method.isAnnotationPresent(RequestMapping.class);

            if (methodHasRequestMapping) {
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

                setRequestMappingInfo(requestMapping, path, requestMethods);
            }

            final RequestMethodsRequestCondition methodsCondition = new RequestMethodsRequestCondition(requestMethods.toArray(RequestMethod[]::new));
            final RequestMappingInfo mapping = new RequestMappingInfo(path.toString(), methodsCondition);

            methods.put(method, mapping);
        });

        return methods;
    }

    private void setRequestMappingInfo(final RequestMapping requestMapping, final StringBuilder path, final Set<RequestMethod> httpMethods) {
        String tempPath = requestMapping.value();

        if (!tempPath.startsWith("/")) path.append("/");
        path.append(tempPath);

        httpMethods.addAll(List.of(requestMapping.methods()));
    }

    private void registerHandlerMethod(final Object handler, final Method method, final RequestMappingInfo mapping) {
        this.mappingRegistry.register(mapping, handler, method);
    }

    @Override
    protected Object getHandlerInternal(final HttpServletRequest req) throws Exception {

        return this.mappingRegistry.getMethod(req.getRequestURI(), RequestMethod.valueOf(req.getMethod()));
    }

    class MappingRegistry {
        private final Map<RequestMappingInfo, HandlerMethod> registry = new HashMap<>();

        public void register(final RequestMappingInfo mapping, final Object handler, final Method method) {
            registry.put(mapping, new HandlerMethod(handler, method));
        }

        public HandlerMethod getMethod(final String url, final RequestMethod requestMethod) {
            for (RequestMappingInfo mapping : registry.keySet()) {
                final boolean match = mapping.isMatch(url, requestMethod);

                if (match) {
                    return this.registry.get(mapping);
                }
            }

            return null;
        }

    }
}
