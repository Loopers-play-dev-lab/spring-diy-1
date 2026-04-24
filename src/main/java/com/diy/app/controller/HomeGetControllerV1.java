package com.diy.app.controller;

import com.diy.framework.web.ControllerV1;
import com.diy.framework.web.Model;
import com.diy.framework.web.RequestBody;

public class HomeGetControllerV1 implements ControllerV1 {
    @Override
    public Model handle(RequestBody body) {
        return new Model("lecture-registration");
    }
}
