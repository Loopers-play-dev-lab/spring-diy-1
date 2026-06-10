package com.diy.framework.web.servlet.handler;

import com.diy.framework.beans.factory.BeanFactoryUtils;
import com.diy.framework.context.ApplicationContext;
import com.diy.framework.context.support.ApplicationObjectSupport;
import com.diy.framework.web.http.converter.HttpMessageConverter;
import com.diy.framework.web.mvc.anotation.ControllerAdvice;
import com.diy.framework.web.mvc.anotation.ExceptionHandler;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExceptionHandlerExceptionResolver extends ApplicationObjectSupport implements HandlerExceptionResolver {

    private final List<HttpMessageConverter> messageConverters;
    private final Map<Class<? extends Throwable>, ExceptionHandlerMethod> exceptionHandlers = new LinkedHashMap<>();

    public ExceptionHandlerExceptionResolver(final List<HttpMessageConverter> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override
    protected void initApplicationContext(final ApplicationContext context) {
        final Map<String, Object> advices = BeanFactoryUtils.beansOfAnnotated(context, ControllerAdvice.class);
        advices.values().forEach(this::detectExceptionHandlerMethods);
    }

    private void detectExceptionHandlerMethods(final Object advice) {
        for (final Method method : advice.getClass().getDeclaredMethods()) {
            final ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);
            if (annotation == null) continue;

            for (final Class<? extends Throwable> exceptionType : annotation.value()) {
                exceptionHandlers.put(exceptionType, new ExceptionHandlerMethod(advice, method));
            }
        }
    }

    @Override
    public ModelAndView resolveException(final HttpServletRequest request,
                                         final HttpServletResponse response,
                                         final Object handler,
                                         final Exception ex) {
        final ExceptionHandlerMethod handlerMethod = findExceptionHandler(ex);
        if (handlerMethod == null) return null;

        try {
            final Object returnValue = handlerMethod.invoke(request, response, ex);
            writeWithMessageConverters(returnValue, request, response);
            return new ModelAndView(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ExceptionHandlerMethod findExceptionHandler(final Exception ex) {
        for (final Map.Entry<Class<? extends Throwable>, ExceptionHandlerMethod> entry : exceptionHandlers.entrySet()) {
            if (entry.getKey().isAssignableFrom(ex.getClass())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void writeWithMessageConverters(final Object body,
                                            final HttpServletRequest request,
                                            final HttpServletResponse response) throws Exception {
        if (body == null) return;

        final String accept = request.getHeader("Accept");
        final Class<?> bodyType = body.getClass();

        for (final HttpMessageConverter converter : messageConverters) {
            if (converter.canWrite(bodyType, accept)) {
                final List<String> supported = converter.getSupportedMediaTypes();
                final String contentType = supported.isEmpty() ? null : supported.getFirst();
                converter.write(body, contentType, response);
                return;
            }
        }

        throw new IllegalStateException("HttpMessageConverter 쓰기 실패: " + bodyType);
    }

    private static final class ExceptionHandlerMethod {

        private final Object bean;
        private final Method method;

        private ExceptionHandlerMethod(final Object bean, final Method method) {
            this.bean = bean;
            this.method = method;
        }

        private Object invoke(final HttpServletRequest request,
                              final HttpServletResponse response,
                              final Exception ex) throws Exception {
            try {
                method.setAccessible(true);

                final Parameter[] parameters = method.getParameters();
                final Object[] args = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    args[i] = resolveArgument(parameters[i].getType(), request, response, ex);
                }

                return method.invoke(bean, args);
            } catch (InvocationTargetException e) {
                final Throwable target = e.getTargetException();
                if (target instanceof Exception ex2) throw ex2;
                throw new RuntimeException(target);
            } finally {
                method.setAccessible(false);
            }
        }

        private Object resolveArgument(final Class<?> type,
                                       final HttpServletRequest request,
                                       final HttpServletResponse response,
                                       final Exception ex) {
            if (HttpServletRequest.class.isAssignableFrom(type)) return request;
            if (HttpServletResponse.class.isAssignableFrom(type)) return response;
            if (type.isAssignableFrom(ex.getClass())) return ex;
            return null;
        }
    }
}
