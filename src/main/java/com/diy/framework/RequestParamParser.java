package com.diy.framework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class RequestParamParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, ?> parse(HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            byte[] bodyBytes = req.getInputStream().readAllBytes();
            String body = new String(bodyBytes, StandardCharsets.UTF_8);
            return objectMapper.readValue(body, new TypeReference<Map<String, Object>>() {});
        }
        return req.getParameterMap();
    }
}
