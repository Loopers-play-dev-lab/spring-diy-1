package com.diy.app.controller;

import com.diy.framework.web.utils.ControllerV1;
import com.diy.framework.web.utils.RequestBodyV1;
import com.diy.framework.web.utils.ResponseV1;

public class HomeGetControllerV1 implements ControllerV1 {
    @Override
    public ResponseV1 handle(RequestBodyV1 body) {
        return new ResponseV1("lecture-registration");
    }
}
