package com.diy.framework.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    void render(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> model) throws Exception;
}
