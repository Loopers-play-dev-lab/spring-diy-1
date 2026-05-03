package com.diy.framework.view;

import javax.servlet.ServletContext;
import java.io.File;

public class JspViewResolver implements ViewResolver {
    private final ServletContext servletContext;

    public JspViewResolver(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public View resolveViewName(String viewName) {
        String path = "/" + viewName + ".jsp";
        // 실제 해당 파일이 존재하는지 확인
        String realPath = servletContext.getRealPath(path);
        if(realPath == null || !new File(realPath).exists()){
            return null;
        }
        return new JspView("/" + path);
    }
}
