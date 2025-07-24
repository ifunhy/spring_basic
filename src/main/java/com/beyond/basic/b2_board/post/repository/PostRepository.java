package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // select * from post where author_id =? and title = ?;
//    List<Post> findByAuthorIdAndTitle(Long author, String title)
    // select * from post where author_id =? and title =? order by createdTime desc;    // 정렬까지 포함
//    List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long author, String title)

//    List<Post> findByAuthorId(Long author);   // 변수명은 author지만 author_id를 조회하는 것도 가능하고
    List<Post> findByAuthor(Author author);   // 객체로 넘겨주는 것도 가능

    // jpql을 사용한 일반 inner join
    // jpa는 기본적으로 lazy로딩을 지향하므로, inner join으로 filtering은 하되, post객체만 조회 -> N+1문제 여전히 발생
    // raw쿼리 : select p.* from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join p.author")
    List<Post> findAllJoin();

    // jpql을 사용한 fetch inner join
    // join 시 post뿐만 아니라 author객체까지 한번에 조립하여 조회 -> N+1문제 해결
    // raw쿼리 : select * from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();

    // paging 처리
    // Pageable 은 org.springframework.data.domain.Pageable 로 import
    // Page객체 안에 List<Post> 포함, 전체 페이지 수 등의 정보 포함
    // Pageable 객체 안에는 페이지size, 페이지번호, 정렬기준 등이 포함
//    Page<Post> findAll(Pageable pageable);
    // paging 처리 + delYn 적용
    Page<Post> findAllByDelYnAndAppointment(Pageable pageable, String delYn, String appointment);

    // paging 처리 + 검색(specification)
    // 검색할 객체, 페이징 처리를 위한 객체를 넘기면 spring에서 자동으로 처리
    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

    List<Post> findByAppointment(String appointment);

}