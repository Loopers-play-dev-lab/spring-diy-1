package com.diy.framework.web.view;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    void render(final Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse resp) throws Exception;
}
