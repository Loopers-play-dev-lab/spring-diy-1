package com.diy.framework.web.view;

import com.diy.framework.web.utils.Model;
import com.diy.framework.web.utils.ResponseV1;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JspView implements ViewV1 {
    public JspView() {
        super();
    }

    public boolean isRender(HttpServletRequest request, String viewName) {
        final ServletContext sc = request.getServletContext();
        String fileName = "/WEB_INF/veiws/" + viewName + ".jsp";
        String realPath = sc.getRealPath(fileName);
        return realPath != null;
    }

    public void render(HttpServletRequest req, HttpServletResponse res, ResponseV1 response) throws Exception {
        String fileName = response.getViewName() + ".jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(fileName);

        Map<String, Object> modelMap = response.getModels();
        for (Map.Entry<String, Object> entry : modelMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            req.setAttribute(key, value);
        }
        dispatcher.forward(req, res);
    }
}
