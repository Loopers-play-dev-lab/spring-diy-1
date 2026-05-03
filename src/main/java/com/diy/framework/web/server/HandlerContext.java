package com.diy.framework.web.server;

record HandlerContext (
        String path,
        HttpMethod method
){
}
