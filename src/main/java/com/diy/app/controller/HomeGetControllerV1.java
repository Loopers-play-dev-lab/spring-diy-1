package com.diy.app.controller;

import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.Model;
import com.diy.framework.web.utils.RequestBody;

public class HomeGetControllerV1 implements ControllerV1 {
    @Override
    public Model handle(RequestBody body) {
        return new Model("lecture-registration");
    }
}
