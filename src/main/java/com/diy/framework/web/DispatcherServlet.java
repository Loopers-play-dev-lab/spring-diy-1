package com.diy.framework.web;

import com.diy.framework.view.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private Map<String, Controller> controllerMap = new HashMap<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public DispatcherServlet(Map<String, Controller> controllerMap) {
        this.controllerMap = controllerMap;

    }

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        viewResolvers.add(new JspViewResolver(sc));
        viewResolvers.add(new HtmlViewResolver(sc));
        viewResolvers.add(new RedirectViewResolver());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[Dispatcher] URI = " + req.getRequestURI() + ", method = " + req.getMethod());

        try {
            Controller controller = controllerMap.get(req.getRequestURI());

            if (controller == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            ModelAndView mav = controller.handleRequest(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String viewName = mav.getViewName();

        final View view = resolveViewName(viewName);

        if(view == null) {
            throw new RuntimeException(viewName + " not found");
        }

        view.render(request, response, mav.getModel());
    }

    private View resolveViewName(String viewName) {
        for(ViewResolver resolver: viewResolvers){
            View view = resolver.resolveViewName(viewName);
            if(view != null){
                return view;
            }
        }
        return null;
    }

//    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
//        if ("application/json".equals(req.getHeader("Content-Type"))) {
//            final byte[] bodyBytes = req.getInputStream().readAllBytes();
//            final String body = new String(bodyBytes, StandardCharsets.UTF_8);
//
//            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
//        } else {
//            return req.getParameterMap();
//        }
//    }
}
