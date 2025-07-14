package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
// 회원 한 명의 정보를 담는 도메인 객체(Entity)
public class Author {
    private Long id;
    private String name;
    private String email;
    private String password;

    public Author(String name, String email, String password) {
        this.id = AuthorMemoryRepository.id;    // 회원마다 고유한 ID 부여, id를 static 으로 세팅해놔서 이렇게 작성함
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updatePw(String password) {
        this.password = password;
    }
}
