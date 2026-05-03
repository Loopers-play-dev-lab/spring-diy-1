package com.diy.app.resolver;

import com.diy.app.render.HtmlView;
import com.diy.app.render.JspView;
import com.diy.app.render.RedirectView;
import com.diy.app.render.View;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewResolver {

    private final List<String> resolveAbles = new ArrayList<>();

    public ViewResolver() {
        resolveAbles.add("jsp");
        resolveAbles.add("html");
        resolveAbles.add("redirect:");
    }

    public View resolveViewName(String viewName) {
        for (String judge : resolveAbles) {

            if (viewName.startsWith(judge)) {
                return new RedirectView(viewName.replaceAll("redirect:", ""));
            }

            String resolveFileName = viewName + "." + judge;
            URL resource = ClassLoader.getSystemResource(resolveFileName);
            if (resource != null) {
                if (judge.equals("jsp")) {
                    return new JspView(resolveFileName);
                } else if (judge.equals("html")) {
                    return new HtmlView(resolveFileName);
                }
            }
        }

        throw new RuntimeException("can not resolve: " + viewName);
    }


}
