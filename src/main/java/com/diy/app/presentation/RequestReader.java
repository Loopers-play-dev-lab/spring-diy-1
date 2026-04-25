package com.diy.app.presentation;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestReader {
    public static String readRequest(HttpServletRequest req) throws IOException {
        byte[] bytes = req.getInputStream().readAllBytes();
        return new String(bytes);
    }
}
