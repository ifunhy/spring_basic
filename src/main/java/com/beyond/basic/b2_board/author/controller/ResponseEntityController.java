//package com.beyond.basic.b2_board.Controller;
//
//import com.beyond.basic.BasicApplication;
//import com.beyond.basic.b2_board.domain.Author;
//import com.beyond.basic.b2_board.dto.CommonDto;
//import org.springframework.boot.SpringApplication;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/response/entity")
//public class ResponseEntityController {
//
//
//
//    // case1. @ResponseStatus 어노테이션 사용
//    @ResponseStatus(HttpStatus.CREATED)
//    @GetMapping("/annotation1")
//    public String annotation() {
//        return ("OK");
//    }
//
//    // case2. 메소드 체이닝 방식
//    @GetMapping("/channing1")
//    public ResponseEntity<?> channing1() {
//        Author author = new Author("test", "test@naver.com", "1234");
//        return (ResponseEntity.status(HttpStatus.CREATED).body(author)); // 상태는 OK, body엔 author 삽입
//    }
//
//    // case3. ResponseEntity 객체를 직접 생성하는 방식(가장 많이 사용)
//    @GetMapping("/custom1")
//    public ResponseEntity<?> cumstom1() {
//        Author author = new Author("test", "test@naver.com", "1234");
//        return (new ResponseEntity<>(author, HttpStatus.CREATED));
//    }
//
//    @GetMapping("/custom2")
//    public ResponseEntity<?> cumstom2() {
//        Author author = new Author("test", "test@naver.com", "1234");
//        return (new ResponseEntity<>(new CommonDto(author, HttpStatus.CREATED.value(), "author is created succesfully"), HttpStatus.CREATED)); // HttpStatus.CREATED.value() : value로 int형변환
//    }
//}
