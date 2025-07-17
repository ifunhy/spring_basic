package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// SpringDataJpa를 사용하기 위해서 JpaRepository를 상속(extends)해야 하고, 상속 시 Entity명과 pk타입을 지정
// JpaRepository를 상속함으로써 JpaRepository의 주요기능(각종 CRUD 기능이 사전 구현) 상속
@Repository     // 싱글톤으로 생성하기 위한 어노테이션
public interface AuthorRepository extends JpaRepository<Author, Long> {
    // save, findAll, findById 등은 사전에 구현되어 있음
    // 그 외의 다른 컬럼으로 조회할 때는 findBy + 컬럼명 형식으로 선언만 하면 실행시점에 자동 구현
    Optional<Author> findByEmail(String Email);
}
