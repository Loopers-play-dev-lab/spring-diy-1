package com.diy.framework.web.render.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface View {
    void render(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws IOException;
}
