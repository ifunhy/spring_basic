package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http request를 분석하고, http response를 생성
@Controller
// 클래스차원의 url 매핑 시에는 RequestMapping 사용
@RequestMapping("/hello")
public class HelloController {
    
    // get요청의 case들
    // case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody 있을 때
    @GetMapping("")   // 아래 메소드에 대한 서버의 엔드포인트를 설정
    // ResponseBody가 없고, return타입이 String인 경우 서버는 templates폴더 밑에 helloworld.html을 찾아서 리턴
    // 데이터가 없으면 화면을 찾아서 보여줌
    @ResponseBody
    public String helloWorld() {
        return ("helloworld");
    }

    // case2. 서버가 사용자에게 String(json형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
        // 직접 json으로 직렬화 할 필요 없이, return 타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
        return (h1);    // 객체를 리턴해주면 json이 나감
    }

    // case3. parameter 방식을 통해 사용자로부터 값을 수신
    // parameter의 형식 : /member?name=hongildong
    @GetMapping("/param")
    @ResponseBody
    // RequestParam : 파라미터로 들어오면, ?name : 으로 들어오면 inputName에 값을 넣어주겠다
    public Hello param(@RequestParam(value = "name")String inputName) {
        // 실습 : {name:사용자가넣은이름, email:아무거나}
        Hello h1 = new Hello(inputName, "abc@naver.com");
        return (h1);
    }

    // case4. pathvariable 방식을 통해 사용자로부터 값을 수신
    // pathvariable의 형식 : /member/1
    // pathvariable 방식은 url을 통해 자원의 구조를 명확하게 표현할 때 사용 (좀 더 restful함)
    @GetMapping("/path/{inputId}")  // inputId 값이 들어오면 아래의 함수에 인자값으로 넣어주겠다
    @ResponseBody
    public String path(@PathVariable String inputId) {
        // 별도의 형변환 없이도, 매개변수에 타입 지정 시 자동 형변환 시켜줌
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return ("OK");
    }
    
    // case5. parameter 2개 이상인 형식
    // /hello/param2?name=hong&email=hong@naver.com 라고 get요청이 들어온다면
     @GetMapping("/param2")
    @ResponseBody
     // value = "" : value가 키값을 의미
    public String param2(@RequestParam(value = "name")String inputName,
                         @RequestParam(value = "email")String inputEmail) {
         System.out.println(inputName);
         System.out.println(inputEmail);
        return ("OK");
     }
}
