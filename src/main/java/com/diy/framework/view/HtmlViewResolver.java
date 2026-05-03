package com.diy.framework.view;

import javax.servlet.ServletContext;
import java.io.File;

public class HtmlViewResolver implements ViewResolver {
    private final ServletContext servletContext;

    public HtmlViewResolver(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public View resolveViewName(String viewName) {
        String path = "/" + viewName + ".html";
        // 실제 해당 파일이 존재하는지 확인
        String realPath = servletContext.getRealPath(path);
        if(realPath == null || !new File(realPath).exists()){
            return null;
        }
        return new HtmlView("/" + path);
    }
}
