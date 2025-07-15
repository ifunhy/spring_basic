package com.beyond.basic.b2_board.dto;

import com.beyond.basic.b2_board.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data   // dto 계층은 데이터의 안정성이 엔티티만큼 중요하지는 않으므로, setter도 일반적으로 추가
// 클라이언트에서 보내는 회원가입 요청 데이터를 담는 클래스
// 회원가입 요청용 DTO
public class AuthorCreateDto {
    private Long id;
    private String name;
    private String email;
    private String password;

    public Author authorToEntity() {
        return (new Author(this.name, this.email, this.password));
    }

}
