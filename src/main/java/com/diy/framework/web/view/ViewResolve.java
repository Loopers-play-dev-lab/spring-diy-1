package com.diy.framework.web.view;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class ViewResolve {
    private final ArrayList<ViewV1> views = new ArrayList<>();

    public ViewResolve() {
        this.views.add(new JspView());
        this.views.add(new HttpView());
    }

    public ViewV1 getView(HttpServletRequest req, String viewName) {
        for (ViewV1 view : views) {
            System.out.println("view : " + view.getClass().getName());
            if(view.isRender(req, viewName)) {
                return view;
            }
        }
        return new EmptyView();
    }
}
