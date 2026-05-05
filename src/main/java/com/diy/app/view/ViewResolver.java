package com.diy.app.view;

public class ViewResolver {

    private final String suffix;
    private final String prefix;

    public ViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public View resolve(String viewName) {
        if (viewName.startsWith("redirect:")) {
            return null;
        }
        String fullpath = prefix + viewName + suffix;

        switch (suffix) {
            case ".jsp":
                return new JspView(fullpath);
            case ".html":
                return new HtmlView(fullpath);
            default:
                throw new IllegalArgumentException("지원하지 않는 뷰 형식입니다: " + suffix);
        }
    }
}
