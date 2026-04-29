package com.diy.app.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws Exception;
}
