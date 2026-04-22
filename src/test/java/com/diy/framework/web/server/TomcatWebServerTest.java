package com.diy.framework.web.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class TomcatWebServerTest {

    @BeforeAll
    static void setUp() {
        TomcatWebServer server = new TomcatWebServer();
        server.start();
    }

    @Test
    @DisplayName("TomcatWebServer를 시작하면 8080 포트에서 응답한다")
    void serverStartsOnPort8080() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:8080").openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());
    }

    @Test
    @DisplayName("TomcatWebServer가 소켓 수준에서 HTTP 응답을 반환한다")
    void serverHandlesRawSocketRequest() throws Exception {
        try (Socket socket = new Socket("localhost", 8080)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("GET / HTTP/1.1");
            out.println("Host: localhost");
            out.println();

            String statusLine = in.readLine();
            assertTrue(statusLine.contains("HTTP/1.1"));
        }
    }
}
