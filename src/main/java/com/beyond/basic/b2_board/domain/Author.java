package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우, Entity 반드시 붙어야 하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// EntityManager는 영속성컨텍스트(Entity의 현재상황)를 통해 DB 데이터 관리
@Entity
// 회원 한 명의 정보를 담는 도메인 객체(Entity)
public class Author {
    @Id     // pk 설정
    private Long id;
    private String name;
    private String email;
    private String password;
//    private String test;
//    private String test2;

    public Author(String name, String email, String password) {
//        this.id = AuthorMemoryRepository.id;    // 회원마다 고유한 ID 부여, id를 static 으로 세팅해놔서 이렇게 작성함
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updatePw(String password) {
        this.password = password;
    }

    public AuthorDetailDto detailFromEntity() {
        return (new AuthorDetailDto(this.id, this.name, this.email));
    }

    public AuthorListDto listFromEntity() {
        return (new AuthorListDto(this.id, this.name, this.email));
    }
}
