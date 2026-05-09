package com.diy.app.controller.v2;

import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.utils.ControllerV2;
import com.diy.framework.web.utils.RequestBodyV1;
import com.diy.framework.web.utils.ResponseV1;


@Controller
public class HomeControllerV2 implements ControllerV2 {
    @Override
    public ResponseV1 handle(String method, RequestBodyV1 body) {
        return new ResponseV1("lecture-registration");
    }
}
