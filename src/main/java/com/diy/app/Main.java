package com.diy.app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        // [자바에서 웹 프로그래밍을 할 때, 서버를 만드는 방법]
        // 자바에서는 ServerSocket이라는 클래스가 있고 이걸로 서버 개발을 할 수 있다!

        // 그럼 이 ServerSocket이라는 클래스로 요청을 어떻게 받을까?
        // 운영체제에서는 소켓도 결국 파일이다
        // 그래서 이 메서드가 하는 역할은 결국, 파일에 문자가 쓰인 걸 가져오는 것
        // 아래의 코드의 역할은 결국, 클라이언트가 우리(서버)한테 보낸 문자열을 받아오는 것
      try  (final ServerSocket serverSocket = new ServerSocket(8080)) {
          final Socket clientSocket = serverSocket.accept();
          // 소켓에 적힌 문자열을 받아오는 것
          final InputStream inputStream = clientSocket.getInputStream();
          // inputStream을 읽는 방법
          final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
          // stream은 순차적으로 정보를 가지고 오는 것
          final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

          final String request = bufferedReader.readLine();
          System.out.println("request = " + request);
          // 여기까지해서 요청을 읽어오는걸 했다

          // 그리고 이제 응답을 줘보자
          final OutputStream outputStream = clientSocket.getOutputStream();
          final PrintWriter printWriter = new PrintWriter(outputStream, true);

          printWriter.println("HTTP/1.1 200 OK");
          printWriter.println("Content-Type: text/html; charset=UTF-8");
          printWriter.println("");
          printWriter.println("<html><body><h1>안녕하세요 안애옹입니다!</h1></body></html>");
      } catch (final IOException e) {
      }
    }
}
// [정리]
// 자바는 추상화를 적극적으로 잘 활용하는 언어이고, 그래서 이런 http 관련 코드도 추상화가 되어있음
// 이렇게 자바는 웹 프로그래밍을 쉽게 제공하기 위해 서블릿이라는 개념을 만들었음