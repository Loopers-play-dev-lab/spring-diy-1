package com.diy.framework.web.servlet;

import com.diy.framework.web.annotations.RequestMethod;

public record RequestHandlerKey(
    String path,
    RequestMethod method
) {
}
