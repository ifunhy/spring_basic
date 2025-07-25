package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data   // dto 계층은 데이터의 안정성이 엔티티만큼 중요하지는 않으므로, setter도 일반적으로 추가
// 클라이언트에서 보내는 회원가입 요청 데이터를 담는 클래스
// 회원가입 요청용 DTO
public class AuthorCreateDto {
    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;
    @NotEmpty(message = "email은 필수 입력 항목입니다.")
    private String email;
    @NotEmpty(message = "password은 필수 입력 항목입니다.")
    @Size(min = 8, message = "password의 길이가 너무 짧습니다.")
    private String password;
//    // 문자열로 값이 넘어오면 Role에 값으로 매핑
//    private Role role = Role.USER;    // 서버를 시작하자마자 USER 계정을 생성하는 실습 위해 주석처리

    // @Builder 어노테이션 실습을 위한 주석처리
//    public Author authorToEntity() {
//        return (new Author(this.name, this.email, this.password, this.role));
//    }

    public Author authorToEntity(String encodedPassword) {
        // @Builder 패턴은 매개변수의 개수와 매개변수의 순서에 상관없이 객체 생성 가능
        return (Author.builder()
                .name(this.name)
                .password(encodedPassword)
                .email(this.email)
                .role(Role.USER)    // default가 USER
                .build()
        );
    }

}
