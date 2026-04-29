package com.diy.app.infra.viewRender;

import com.diy.app.infra.dto.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewResolver {

    private static ViewResolver instance;
    private static final List<View> views = List.of(HtmlView.getInstance(), JspView.getInstance(), RedirectView.getInstance());

    public static ViewResolver getInstance() {
        if (Objects.isNull(instance)) instance = new ViewResolver();
        return instance;
    }

    public void resolve(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView modelAndView) throws IOException, ServletException {
        views.stream()
                .filter(view -> view.match(req, resp, modelAndView))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("View가 존재하지 않습니다."))
                .render(req, resp, modelAndView);
    }

    public void setReq(final HttpServletRequest req, final ModelAndView modelAndView) {
        for (Map.Entry<String, Object> entry : modelAndView.getModel().entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
