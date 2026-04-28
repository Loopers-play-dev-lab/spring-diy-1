package com.diy.app.resolver;

import com.diy.app.render.HtmlView;
import com.diy.app.render.JspView;
import com.diy.app.render.View;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewResolver {

    private final List<String> resolveAbles = new ArrayList<>();

    public ViewResolver() {
        resolveAbles.add("jsp");
        resolveAbles.add("html");
    }

    public View resolveViewName(String viewName) {
        for (String ext : resolveAbles) {
            String resolveFileName = viewName + "." + ext;
            URL resource = ClassLoader.getSystemResource(resolveFileName);
            if (resource != null) {
                if (ext.equals("jsp")) {
                    return new JspView(resolveFileName);
                } else if (ext.equals("html")) {
                    return new HtmlView(resolveFileName);
                }
            }
        }

        throw new RuntimeException("can not resolve: " + viewName);
    }


}
