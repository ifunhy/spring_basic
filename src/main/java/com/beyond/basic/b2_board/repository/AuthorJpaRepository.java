package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJpaRepository {
    @Autowired  // 생성자주입 방식으로 DI
    private EntityManager entityManager;

    public void save(Author author) {
        // persist : 순수jpa에서 데이터를 insert 하는 메소드
        entityManager.persist(author);
    }

    public List<Author> findAll() {
        // 순수jpa에서는 제한된 메소드 제공으로, jpql을 사용하여 직접 쿼리를 작성하는 경우가 많음.
        // jpql : jpql 문법은 문자열 형식의 raw쿼리가 아닌 객체지향쿼리문

    }

    public Optional<Author> findById(Long id) {
        Author author = entityManager.find(Author.class, id);
        return (Optional.ofNullable(author));   // 찾았으면 author, 못찾았으면 null 리턴

    }

    public Optional<Author> findByEmail(String email){

    }

    public void delete(Long id){

    }
}
