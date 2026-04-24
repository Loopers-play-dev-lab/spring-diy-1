package com.diy.framework.web.view;

import com.diy.framework.web.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class JspView implements View {
    public JspView() {
        super();
    }

    public boolean isRender(HttpServletRequest request, String viewName) {
        final ServletContext sc = request.getServletContext();
        String fileName = "/WEB_INF/veiws/" + viewName + ".jsp";
        String realPath = sc.getRealPath(fileName);
        return realPath != null;
    }

    public void render(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
        String fileName = model.getViewName() + ".jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(fileName);

        Map<String, Object> modelMap = model.getModels();
        for (Map.Entry<String, Object> entry : modelMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            req.setAttribute(key, value);
        }
        dispatcher.forward(req, res);
    }
}
