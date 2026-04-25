package com.diy.app.infra.viewRender;

import com.diy.app.infra.dto.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ViewResolver {

    private static ViewResolver instance;

    public static ViewResolver getInstance() {
        if (Objects.isNull(instance)) instance = new ViewResolver();
        return instance;
    }

    public void resolve(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView modelAndView) throws IOException, ServletException {
        URL htmlFile = getClass().getClassLoader().getResource(modelAndView.getViewName() + ".html");
        URL jspFile = getClass().getClassLoader().getResource(modelAndView.getViewName() + ".jsp");

        System.out.println("htmlFile = " + htmlFile);
        System.out.println("jspFile = " + jspFile);

        View view = null;

        if (Objects.nonNull(jspFile)) view = new JspView(jspFile.getFile().split("resources/")[1]);
        else if (Objects.nonNull(htmlFile)) view = new HtmlView(htmlFile.getFile().split("resources/")[1]);
        else throw new IllegalArgumentException("파일이 존재하지 않습니다.");

        view.render(req, resp);
    }
}
