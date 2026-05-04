package com.diy.framework.web.viewResolver;

import com.diy.framework.web.view.RedirectView;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.JspView;

public class JspViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String viewName) {
        if (viewName.startsWith("redirect:")){
			System.out.println("Redirecting to: " + viewName.substring(9));
			return new RedirectView(viewName.substring(9));
		}

		return new JspView("/" + viewName + ".jsp");
    }
}
