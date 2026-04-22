package com.diy.app.presentation;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestReader {
    public static String readRequest(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
