package com.diy.framework.controller;

import com.diy.framework.enums.RequestMethod;

public record RequestKey(
        String path,
        RequestMethod httpMethod
) {
}
