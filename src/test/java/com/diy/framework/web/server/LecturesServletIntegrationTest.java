package com.diy.framework.web.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LecturesServletIntegrationTest {

    private static TomcatWebServer server;
    private static final String BASE_URL = "http://localhost:8084/lectures";

    @BeforeAll
    static void startServer() throws InterruptedException {
        server = new TomcatWebServer();
        server.start();
        Thread.sleep(500);
    }

    @Test
    void GET_강의_목록_조회() throws Exception {
        HttpURLConnection conn = openConnection("GET");

        assertEquals(200, conn.getResponseCode());
        assertEquals("text/html;charset=UTF-8", conn.getContentType());
        assertTrue(readBody(conn).contains("<title>강의 목록</title>"));
    }

    @Test
    void POST_강의_등록() throws Exception {
        HttpURLConnection postConn = openConnection("POST");
        postConn.setDoOutput(true);
        postConn.setRequestProperty("Content-Type", "application/json");

        String json = "{\"name\":\"Java\",\"price\":\"10000\"}";
        postConn.getOutputStream().write(json.getBytes());

        assertEquals(200, postConn.getResponseCode());

        HttpURLConnection getConn = openConnection("GET");
        assertTrue(readBody(getConn).contains("Java"));
    }

    @Test
    void PUT_강의_수정() throws Exception {
        HttpURLConnection conn = openConnection("PUT");
        conn.setDoOutput(true);

        assertEquals(200, conn.getResponseCode());
        assertEquals("put lectures", readBody(conn));
    }

    @Test
    void DELETE_강의_삭제() throws Exception {
        HttpURLConnection conn = openConnection("DELETE");

        assertEquals(200, conn.getResponseCode());
        assertEquals("delete lectures", readBody(conn));
    }

    private HttpURLConnection openConnection(String method) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        return conn;
    }

    private String readBody(HttpURLConnection conn) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return br.lines().collect(Collectors.joining());
        }
    }
}
