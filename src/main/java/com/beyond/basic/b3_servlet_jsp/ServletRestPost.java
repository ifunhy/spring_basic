package com.beyond.basic.b3_servlet_jsp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// 서블릿은 사용자의 req를 쉽게 처리하고, 사용자에 res를 쉽게 조립해 주는 기술
// 서블릿에서는 url매핑을 메소드 단위가 아닌, 클래스 단위로 지정
@WebServlet("/servlet/post")
public class ServletRestPost extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // parameter로 받으면 리턴타입은 url인코딩 방식 또는 multipart-formdata
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        System.out.println(name);
        System.out.println(email);
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print("ok");
        printWriter.flush();
    }
}
