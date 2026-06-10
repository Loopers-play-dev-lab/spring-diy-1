package com.diy.framework.web.server;

public class WebServerException extends RuntimeException {
    public WebServerException(final String message, final Throwable t) {
        super(message, t);
    }
}
