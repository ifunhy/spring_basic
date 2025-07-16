package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import jakarta.persistence.*;
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
    // identity : auto_increment, auto : id 생성전략을 jpa에게 자동설정하도록 위임하는 것
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 컬럼에 별다른 설정이 없을 경우 default varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)   // 길이 50 한도, unique, notnull 설정
    private String email;
//    @Column(name = "pw") : 되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있음
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
