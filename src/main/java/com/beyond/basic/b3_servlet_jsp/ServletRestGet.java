package com.beyond.basic.b3_servlet_jsp;

import com.beyond.basic.b1_hello.controller.Hello;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// 서블릿은 사용자의 req를 쉽게 처리하고, 사용자에 res를 쉽게 조립해 주는 기술
// 서블릿에서는 url매핑을 메소드 단위가 아닌, 클래스 단위로 지정
@WebServlet("/servlet/get")
public class ServletRestGet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Hello hello = new Hello();
        hello.setName("hongildong");
        hello.setEmail("hong@naver.com");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(hello);   // writeValueAsString() : 객체를 json으로 변환하는 메소드

        PrintWriter printWriter = resp.getWriter(); // 리턴하기 위한 설정
        printWriter.println(body); // 리턴값
        printWriter.flush();    // BufferedWriter 라서 flush() 해줌
    }
}
