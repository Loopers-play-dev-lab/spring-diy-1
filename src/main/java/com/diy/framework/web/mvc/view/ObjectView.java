package com.diy.framework.web.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ObjectView implements View{
    private final String viewName;

    public ObjectView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest req, HttpServletResponse res) throws Exception {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(model);

        res.getWriter().write(jsonString);
        res.getWriter().flush();
    }
}
