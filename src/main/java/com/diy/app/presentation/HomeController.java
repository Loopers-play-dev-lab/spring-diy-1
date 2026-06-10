package com.diy.app.presentation;

import com.diy.framework.web.beans.annotations.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;

import java.util.Map;

@Controller(url = "/home")
public class HomeController {
    public HomeController() {}

    public ModelAndView handleRequest(String method, Map<String, ?> params) {
        System.out.println("Home");
        return new ModelAndView("home");
    }
}