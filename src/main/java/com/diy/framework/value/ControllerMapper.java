package com.diy.framework.value;

import com.diy.framework.controller.Controller;

public record ControllerMapper(
        String uri,
        Controller controller
) {
}
