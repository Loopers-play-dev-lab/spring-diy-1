package com.diy.framework.web.mvc.view;

import com.diy.framework.web.mvc.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    void render(final HttpServletRequest req, final HttpServletResponse res, final Map<String, Object> model) throws Exception;
}