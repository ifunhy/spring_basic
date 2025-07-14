package com.beyond.basic.b2_board.Controller;

import com.beyond.basic.BasicApplication;
import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
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
    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

    private final AuthorService authorService;  // 서비스 주입 받기

    // 회원가입
    @PostMapping("/create")
    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
        try {
            this.authorService.save(authorCreateDto);
        } catch (Exception e) {  // 모든 예외를 포괄적으로 잡음
            e.printStackTrace();
            return ("오류 발생: " + e.getMessage());
        }
        return "회원가입 완료";
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
    public AuthorDetailDto findById(@PathVariable Long id) {
        try {
            return (authorService.findById(id));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return (null);
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
