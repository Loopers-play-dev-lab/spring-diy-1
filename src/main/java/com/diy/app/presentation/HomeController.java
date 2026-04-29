package com.diy.app.presentation;

import com.diy.framework.web.mvc.servlet.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;

import java.util.Map;

public class HomeController implements Controller {
    private static HomeController instance;
    private HomeController() {}
    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }
        return instance;
    }

    @Override
    public ModelAndView handleRequest(String method, Map<String, ?> params) {
        System.out.println("Home");
        return new ModelAndView("home");
    }
}