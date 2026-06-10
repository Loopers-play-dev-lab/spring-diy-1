package com.diy.framework.web.http.converter.json;

import com.diy.framework.web.http.converter.HttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MappingJackson2HttpMessageConverter implements HttpMessageConverter<Object> {

    private static final String APPLICATION_JSON = "application/json";

    private final ObjectMapper objectMapper;

    public MappingJackson2HttpMessageConverter() {
        this(new ObjectMapper());
    }

    public MappingJackson2HttpMessageConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean canRead(final Class<?> clazz, final String contentType) {
        if (contentType == null || contentType.isBlank()) return false;
        return contentType.contains(APPLICATION_JSON);
    }

    @Override
    public boolean canWrite(final Class<?> clazz, final String acceptHeader) {
        if (acceptHeader == null || acceptHeader.isBlank()) return true;
        return acceptHeader.contains(APPLICATION_JSON) || acceptHeader.contains("*/*");
    }

    @Override
    public List<String> getSupportedMediaTypes() {
        return List.of(APPLICATION_JSON);
    }

    @Override
    public Object read(final Class<?> clazz, final HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getInputStream(), clazz);
    }

    @Override
    public void write(final Object body, final String contentType, final HttpServletResponse response) throws IOException {
        final String resolved = (contentType == null || contentType.isBlank())
                ? APPLICATION_JSON + ";charset=UTF-8"
                : contentType;
        response.setContentType(resolved);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
