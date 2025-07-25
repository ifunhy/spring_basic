package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.author.dto.CommonErrorDto;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.common.JwtTokenFilter;
import com.beyond.basic.b2_board.common.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/create")
    // dto에 있는 validation(검증용) 어노테이션(@NotEmpty @Size 등)과 controller @Valid 한쌍
    // 이걸 붙이면, Spring이 MemberCreateDto 안의 검증 어노테이션을 자동으로 실행
    /* 아래 코드 포스트맨 테이스 데이터 예시
    1. multipart-formdata 선택
    2. authorCreateDto를 text로 {"name":"test2", "email":"test2@naver.com", "password":"12341234"}
       세팅하면서 content-type을 application/json 설정
    3. profileImage는 file로 세팅하면서 content-type을 multipart/form-data 설정
    */
    public ResponseEntity<String> save(@RequestPart(name = "authorCreateDto") @Valid AuthorCreateDto authorCreateDto,
                                       @RequestPart(name = "profileImage", required = false)MultipartFile profileImage
                                      ) {   // required = false 넣어도 되고 안 넣어도 됨
        System.out.println(profileImage.getOriginalFilename());
//        try {
//            this.authorService.save(authorCreateDto);
//            return (new ResponseEntity<>("OK", HttpStatus.CREATED));
//        } catch (IllegalArgumentException e) {  // 모든 예외를 포괄적으로 잡음
//            e.printStackTrace();
//            // 생성자 매개변수로 응답을 줄 body부분의 객체와 header부에 상태 코드를 삽입
//            return (new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
//        }
        // ControllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외처리가 가능
        this.authorService.save(authorCreateDto, profileImage);

        return (new ResponseEntity<>("OK", HttpStatus.CREATED));
    }

    // 로그인
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody AuthorLoginDto dto) {   // ResponseEntity 안에는 토큰이 담겨야 함
        Author author = authorService.doLogin(dto);
        // 토큰 생성 및 return
        String token = jwtTokenProvider.createAtToken(author);

        return (new ResponseEntity<>(new CommonDto(token, HttpStatus.OK.value(), "token is created")
                , HttpStatus.OK));
    }

    // 회원목록조회 : url 패턴("/author/list")
    @GetMapping("/list")
    // ADMIN 권한이 있는지를 authentication 객체에서 쉽게 확인
    // 권한이 없을 경우 filterchain에서 에러 발생
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthorListDto> findAll() {
        return (authorService.findAll());
    }

    // 회원상세조회 : id로 조회, url 패턴("author/detail/1")
    // 서버에서 별도의 try-catch를 하지 않으면, 에러 발생시 500에러 +  스프링의 포맷으로 에러를 리턴
    // @GetMapping
    @GetMapping("/detail/{id}")
    // ADMIN 권한이 있는지를 authentication 객체에서 쉽게 확인
    // 여러 권한 설정하고 싶을 때 -> "hasRole('ADMIN') or hadRole('SELLER')"
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
//            return new (new ResponseEntity<CommonDto>())
            return (new ResponseEntity<>(authorService.myinfo(), HttpStatus.OK));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            CommonErrorDto error = new CommonErrorDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return new ResponseEntity<CommonErrorDto>(error, HttpStatus.NOT_FOUND);
            // return (new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND));
        }
    }

    // 내 정보 조회
    @GetMapping("/myinfo")
    public ResponseEntity<?> myinfo() {
        return (new ResponseEntity<>(authorService.myinfo(), HttpStatus.OK));
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
