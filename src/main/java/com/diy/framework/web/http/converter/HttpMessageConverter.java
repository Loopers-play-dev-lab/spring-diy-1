package com.diy.framework.web.http.converter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface HttpMessageConverter<T> {

    boolean canRead(Class<?> clazz, String contentType);

    boolean canWrite(Class<?> clazz, String acceptHeader);

    List<String> getSupportedMediaTypes();

    T read(Class<? extends T> clazz, HttpServletRequest request) throws IOException;

    void write(T body, String contentType, HttpServletResponse response) throws IOException;
}
