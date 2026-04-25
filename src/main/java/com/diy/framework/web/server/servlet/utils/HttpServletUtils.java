package com.diy.framework.web.server.servlet.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class HttpServletUtils {

  public static Map<String, ?> parseBody(final HttpServletRequest req) throws IOException {
    if ("application/json".equals(req.getHeader("Content-Type"))) {
      final byte[] bodyBytes = req.getInputStream().readAllBytes();
      final String body = new String(bodyBytes, StandardCharsets.UTF_8);

      return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
    } else {
      return req.getParameterMap();
    }
  }

  public static String getUriPath(final HttpServletRequest req) {
    final String originPath = req.getRequestURI();
    final String trimPath = originPath.replace("%20", "");
    if (trimPath.endsWith("/")) return trimPath.substring(0, trimPath.length() - 1);
    return trimPath;
  }
}
