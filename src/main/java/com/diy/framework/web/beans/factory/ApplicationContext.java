package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotations.RequestMapping;
import com.diy.framework.web.annotations.RequestMethod;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.servlet.DispatcherServlet;
import com.diy.framework.web.servlet.HandlerExecution;
import com.diy.framework.web.servlet.RequestHandlerKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(final String... basePackages) {
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry();
        BeanDefinitionReader definitionReader = new BeanDefinitionReader(new BeanScanner(basePackages), registry);
        definitionReader.loadBeanDefinitions();

        this.beanFactory = new BeanFactory(registry);
        beanFactory.preInstantiateSingletons();
    }

    public void initialize() {
        final DispatcherServlet servlet = new DispatcherServlet(resolveHandlerExecutions());
        final TomcatWebServer tomcatWebServer = new TomcatWebServer(servlet);
        tomcatWebServer.start();
    }

    private Map<RequestHandlerKey, HandlerExecution> resolveHandlerExecutions() {
        Map<RequestHandlerKey, HandlerExecution> handlerExecutions = new LinkedHashMap<>();

        registerInterfaceControllers(handlerExecutions);
        registerAnnotationControllers(handlerExecutions);
        return handlerExecutions;
    }

    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Object getBean(final String name) {
        return beanFactory.getBean(name);
    }

    public <T> Map<String, T> getBeansOfType(final Class<T> type) {
        return beanFactory.getBeansOfType(type);
    }

    public Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> annotationType) {
        return beanFactory.getBeansWithAnnotation(annotationType);
    }

    private void registerInterfaceControllers(final Map<RequestHandlerKey, HandlerExecution> handlerExecutions) {
        for (com.diy.framework.web.mvc.Controller controller : getBeansOfType(com.diy.framework.web.mvc.Controller.class).values()) {
            List<Method> requestMappingMethods = findRequestMappingMethods(controller.getClass());
            if (!requestMappingMethods.isEmpty()) {
                continue;
            }

            RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                throw new IllegalStateException("컨트롤러에 @RequestMapping이 없습니다: " + controller.getClass().getName());
            }

            registerHandlerExecution(
                handlerExecutions,
                requestMapping.value(),
                resolveRequestMethods(requestMapping.methods()),
                controller.getClass().getName(),
                controller::handleRequest
            );
        }
    }

    private void registerAnnotationControllers(final Map<RequestHandlerKey, HandlerExecution> handlerExecutions) {
        for (Object controller : getBeansWithAnnotation(com.diy.framework.web.annotations.Controller.class).values()) {
            List<Method> requestMappingMethods = findRequestMappingMethods(controller.getClass());
            if (requestMappingMethods.isEmpty()) {
                continue;
            }

            String classPath = resolveClassPath(controller.getClass());
            for (Method method : requestMappingMethods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String requestPath = combinePaths(classPath, requestMapping.value());

                registerHandlerExecution(
                    handlerExecutions,
                    requestPath,
                    resolveRequestMethods(requestMapping.methods()),
                    controller.getClass().getName() + "#" + method.getName(),
                    (request, response) -> invokeControllerMethod(controller, method, request, response)
                );
            }
        }
    }

    private List<Method> findRequestMappingMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .toList();
    }

    private String resolveClassPath(final Class<?> controllerClass) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return "";
        }

        return requestMapping.value();
    }

    private RequestMethod[] resolveRequestMethods(final RequestMethod[] requestMethods) {
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    private void registerHandlerExecution(
        final Map<RequestHandlerKey, HandlerExecution> handlerExecutions,
        final String path,
        final RequestMethod[] requestMethods,
        final String handlerName,
        final HandlerExecution handlerExecution
    ) {
        String normalizedPath = normalizePath(path);

        for (RequestMethod requestMethod : requestMethods) {
            RequestHandlerKey key = new RequestHandlerKey(normalizedPath, requestMethod);
            if (handlerExecutions.containsKey(key)) {
                throw new IllegalStateException("같은 요청 경로와 메서드의 핸들러가 이미 등록되어 있습니다: "
                    + requestMethod + " " + normalizedPath);
            }

            handlerExecutions.put(key, handlerExecution);
        }
    }

    private ModelAndView invokeControllerMethod(
        final Object controller,
        final Method method,
        final javax.servlet.http.HttpServletRequest request,
        final javax.servlet.http.HttpServletResponse response
    ) throws Exception {
        validateControllerMethod(method);

        try {
            method.setAccessible(true);
            Object result = switch (method.getParameterCount()) {
                case 0 -> method.invoke(controller);
                case 2 -> method.invoke(controller, request, response);
                default -> throw new IllegalStateException("지원하지 않는 컨트롤러 메서드 시그니처입니다: " + method);
            };

            return (ModelAndView) result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("컨트롤러 메서드 호출에 실패했습니다: " + method, e);
        }
    }

    private void validateControllerMethod(final Method method) {
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException("컨트롤러 메서드는 ModelAndView를 반환해야 합니다: " + method);
        }

        if (method.getParameterCount() == 0) {
            return;
        }

        if (method.getParameterCount() == 2
            && javax.servlet.http.HttpServletRequest.class.isAssignableFrom(method.getParameterTypes()[0])
            && javax.servlet.http.HttpServletResponse.class.isAssignableFrom(method.getParameterTypes()[1])) {
            return;
        }

        throw new IllegalStateException("지원하지 않는 컨트롤러 메서드 시그니처입니다: " + method);
    }

    private String combinePaths(final String classPath, final String methodPath) {
        String normalizedClassPath = normalizePath(classPath);
        String normalizedMethodPath = normalizePath(methodPath);

        if ("/".equals(normalizedClassPath) && "/".equals(normalizedMethodPath)) {
            throw new IllegalStateException("컨트롤러 경로가 비어 있습니다.");
        }

        if ("/".equals(normalizedClassPath)) {
            return normalizedMethodPath;
        }

        if ("/".equals(normalizedMethodPath)) {
            return normalizedClassPath;
        }

        return normalizedClassPath + normalizedMethodPath;
    }

    private String normalizePath(final String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }

        if ("/".equals(path)) {
            return "/";
        }

        return path.startsWith("/") ? path : "/" + path;
    }
}
