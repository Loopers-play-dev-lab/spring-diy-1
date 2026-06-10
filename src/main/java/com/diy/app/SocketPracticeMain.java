package com.diy.app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//public class SocketPracticeMain {
//    public static void main(String[] args) {
//        try (final ServerSocket serverSocket = new ServerSocket(8080)) {
//            while (true) {
//                final Socket clientSocket = serverSocket.accept();
//                final InputStream inputStream = clientSocket.getInputStream();
//                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                final PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
//
//                final String request = bufferedReader.readLine();
//                System.out.println("request : " + request);
//
//                printWriter.println("HTTP/1.1 200 OK");
//                printWriter.println("Content-Type: text/html; charset=UTF-8");
//                printWriter.println();
//                printWriter.println("<html><body><h1>Hello, World!</h1></body></html>");
//                printWriter.flush();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
