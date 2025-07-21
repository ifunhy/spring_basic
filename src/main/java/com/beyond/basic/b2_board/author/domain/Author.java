package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우, Entity 반드시 붙어야 하는 어노테이션
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// EntityManager는 영속성컨텍스트(Entity의 현재상황)를 통해 DB 데이터 관리
@Entity
@Builder    // Builder어노테이션을 통해 유연하게 객체 생성 가능
// 회원 한 명의 정보를 담는 도메인 객체(Entity)
public class Author extends BaseTimeEntity {
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
    @Enumerated(EnumType.STRING)
    @Builder.Default    // Builder 패턴에서 변수 초기화(디폴트값) 시 Builder.Default어노테이션 필수
    private Role role = Role.USER;  // default를 USER로 설정


    // OneToMany는 선택사항, 또한 default가 lazy
    // 1:n 관계, mappedBy 어디가와 매핑되어 있다
    // mappedBy에는 ManyToOne쪽에 변수명을 문자열로 지정. FK 관리를 반대편(post)쪽에서 한다는 의미 -> 연관관계의 주인 설정
    // cascade : 부모객체의 변화에 따라 자식객체가 같이 변하는 옵션 1)persist : 저장   2)remove : 삭제
//    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)  // orphanRemoval = true를 위한 주석처리
    // 자식의 자식까지 모두 삭제할 경우 orphanRemoval = true 옵션 추가
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Post> postList = new ArrayList<>();    //@OneToMany 쓸 때 List 초기화 필수
    
    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)  // mappedBy = "author" (Address 쪽에 쓰인 이름을 사용)
    private Address address;

//    // @Builder 어노테이션 실습을 위한 주석처리
//    public Author(String name, String email, String password) {
////        this.id = AuthorMemoryRepository.id;    // 회원마다 고유한 ID 부여, id를 static 으로 세팅해놔서 이렇게 작성함
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    public Author(String name, String email, String password, Role role) {
////        this.id = AuthorMemoryRepository.id;    // 회원마다 고유한 ID 부여, id를 static 으로 세팅해놔서 이렇게 작성함
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.role = role;
//    }

    public void updatePw(String password) {
        this.password = password;
    }

//    public AuthorDetailDto detailFromEntity() {
//        return (new AuthorDetailDto(this.id, this.name, this.email, this.role));
//    }

    public AuthorListDto listFromEntity() {
        return (new AuthorListDto(this.id, this.name, this.email, this.role));
    }
}
