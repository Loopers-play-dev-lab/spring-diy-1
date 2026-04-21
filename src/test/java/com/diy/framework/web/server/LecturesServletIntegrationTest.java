package com.diy.framework.web.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void GET_강의_목록_조회() throws Exception {
        HttpURLConnection conn = openConnection("GET", BASE_URL);

        assertEquals(200, conn.getResponseCode());
        assertEquals("text/html;charset=UTF-8", conn.getContentType());
        assertTrue(readBody(conn).contains("<title>강의 목록</title>"));
    }

    @Test
    @Order(2)
    void POST_강의_등록() throws Exception {
        HttpURLConnection postConn = openConnection("POST", BASE_URL);
        postConn.setDoOutput(true);
        postConn.setRequestProperty("Content-Type", "application/json");

        String json = "{\"name\":\"Java\",\"price\":\"10000\"}";
        postConn.getOutputStream().write(json.getBytes());

        assertEquals(200, postConn.getResponseCode());

        HttpURLConnection getConn = openConnection("GET", BASE_URL);
        assertTrue(readBody(getConn).contains("Java"));
    }

    @Test
    @Order(3)
    void PUT_강의_수정() throws Exception {
        HttpURLConnection putConn = openConnection("PUT", BASE_URL + "?id=1");
        putConn.setDoOutput(true);
        putConn.setRequestProperty("Content-Type", "application/json");

        String json = "{\"name\":\"Spring\",\"price\":\"20000\"}";
        putConn.getOutputStream().write(json.getBytes());

        assertEquals(200, putConn.getResponseCode());

        HttpURLConnection getConn = openConnection("GET", BASE_URL);
        assertTrue(readBody(getConn).contains("Spring"));
    }

    @Test
    @Order(4)
    void DELETE_강의_삭제() throws Exception {
        HttpURLConnection deleteConn = openConnection("DELETE", BASE_URL + "?id=1");

        assertEquals(200, deleteConn.getResponseCode());

        HttpURLConnection getConn = openConnection("GET", BASE_URL);
        assertFalse(readBody(getConn).contains("Spring"));
    }

    private HttpURLConnection openConnection(String method, String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
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
