package com.diy.framework.web.mvc.method;

import com.diy.framework.web.http.converter.HttpMessageConverter;
import com.diy.framework.web.method.HandlerMethod;
import com.diy.framework.web.method.support.HandlerMethodArgumentResolver;
import com.diy.framework.web.method.support.HandlerMethodReturnValueHandler;
import com.diy.framework.web.mvc.anotation.RequestBody;
import com.diy.framework.web.mvc.anotation.ResponseBody;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestResponseBodyMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {

    private final List<HttpMessageConverter> messageConverters;

    public RequestResponseBodyMethodProcessor(final List<HttpMessageConverter> messageConverters) {
        this.messageConverters = messageConverters;
    }

    @Override
    public boolean supportsParameter(final Parameter parameter) {
        return parameter.isAnnotationPresent(RequestBody.class);
    }

    @Override
    public Object resolveArgument(final Parameter parameter,
                                  final HttpServletRequest request,
                                  final HttpServletResponse response) throws Exception {
        return readWithMessageConverters(parameter.getType(), request);
    }

    @Override
    public boolean supportsReturnType(final HandlerMethod handlerMethod) {
        return hasResponseBody(handlerMethod.getMethod().getDeclaringClass())
                || handlerMethod.getMethod().isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public ModelAndView handleReturnValue(final Object returnValue,
                                          final HandlerMethod handlerMethod,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) throws Exception {
        writeWithMessageConverters(returnValue, request, response);

        return null;
    }

    private Object readWithMessageConverters(final Class<?> paramType,
                                             final HttpServletRequest request) throws Exception {
        final String contentType = request.getContentType();

        for (final HttpMessageConverter converter : messageConverters) {
            if (converter.canRead(paramType, contentType)) {
                return converter.read(paramType, request);
            }
        }

        throw new IllegalStateException("HttpMessageConverter 읽기 실패: " + paramType);
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

    private boolean hasResponseBody(final Class<?> clazz) {
        return isAnnotationPresentIncludingMeta(clazz, ResponseBody.class);
    }

    private boolean isAnnotationPresentIncludingMeta(final Class<?> clazz,
                                                     final Class<? extends Annotation> target) {
        if (clazz.isAnnotationPresent(target)) return true;
        final Set<Class<? extends Annotation>> visited = new HashSet<>();
        for (final Annotation ann : clazz.getAnnotations()) {
            if (hasAnnotationIncludingMeta(ann, target, visited)) return true;
        }
        return false;
    }

    private boolean hasAnnotationIncludingMeta(final Annotation ann,
                                               final Class<? extends Annotation> target,
                                               final Set<Class<? extends Annotation>> visited) {
        final Class<? extends Annotation> type = ann.annotationType();
        if (!visited.add(type)) return false;
        if (type.equals(target)) return true;
        for (final Annotation meta : type.getAnnotations()) {
            if (hasAnnotationIncludingMeta(meta, target, visited)) return true;
        }
        return false;
    }
}
