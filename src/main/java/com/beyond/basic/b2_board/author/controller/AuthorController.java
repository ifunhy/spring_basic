package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.author.dto.CommonErrorDto;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SpringBootApplication
//@Controller // 싱글톤 객체로 만들기 위해 어노테이션 사용 + request 분석, response 해주기 위함
@RestController // Controller + ResponseBody -> 데이터만 주는 방식으로 구현할 때 필요
@RequiredArgsConstructor
@RequestMapping("/author")
// HTTP 요청 처리
// 사용자의 웹 요청을 받아 Service로 전달하고 응답을 리턴
public class AuthorController {

    private final AuthorService authorService;  // 서비스 주입 받기

    // 회원가입
    @PostMapping("/create")
    // dto에 있는 validation어노테이션과 controller @Valid 한쌍
    public ResponseEntity<String> save(@Valid @RequestBody AuthorCreateDto authorCreateDto) {
//        try {
//            this.authorService.save(authorCreateDto);
//            return (new ResponseEntity<>("OK", HttpStatus.CREATED));
//        } catch (IllegalArgumentException e) {  // 모든 예외를 포괄적으로 잡음
//            e.printStackTrace();
//            // 생성자 매개변수로 응답을 줄 body부분의 객체와 header부에 상태 코드를 삽입
//            return (new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
//        }
        // ControllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외처리가 가능
        this.authorService.save(authorCreateDto);

        return (new ResponseEntity<>("OK", HttpStatus.CREATED));
    }
    // postman으로 데이터 요청

    // 회원목록조회 : url 패턴("/author/list")
    @GetMapping("/list")
    public List<AuthorListDto> findAll() {
        return (authorService.findAll());
    }

    // 회원상세조회 : id로 조회, url 패턴("author/detail/1")
    // 서버에서 별도의 try-catch를 하지 않으면, 에러 발생시 500에러 +  스프링의 포맷으로 에러를 리턴
    // @GetMapping
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
//            return new (new ResponseEntity<CommonDto>())
            return (new ResponseEntity<>(authorService.findById(id), HttpStatus.OK));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            CommonErrorDto error = new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<CommonErrorDto>(error, HttpStatus.NOT_FOUND);
            // return (new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND));
        }
    }

    // 비밀번호수정 : url 패턴("/updatepw"), email/password로 수정할 수 있게 설정 -> json
    // get : 조회 / post : 등록 / patch : 부분 수정 / put : 대체 / delete : 삭제
    @PatchMapping("/updatepw")
    public void updatePw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        authorService.updatePassword(authorUpdatePwDto);
    }

    // 회원탈퇴(삭제) : url 패턴("/author/delete/1")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
