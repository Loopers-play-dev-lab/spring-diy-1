package com.diy.framework.web.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestParam {
    public static RequestBodyV1 parseBody(HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            byte[] bodyBytes = req.getInputStream().readAllBytes();
            String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return RequestBodyV1.of(
                    new ObjectMapper()
                            .readValue(body, new TypeReference<Map<String, Object>>() {})
            );
        } else {
            return RequestBodyV1.of(
                    new HashMap<>()
            );
        }
    }
}
