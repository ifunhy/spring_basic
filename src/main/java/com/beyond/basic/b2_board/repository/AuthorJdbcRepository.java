package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository // 싱글톤 객체로 만들기 위한 어노테이션
public class AuthorJdbcRepository {

    // Datasource는 DB와 JDBC에서 사용하는 DB연결 드라이버 객체
    // application.yml에 설정한 DB정보를 사용하여 dataSource객체 싱글톤 생성
    @Autowired
    private DataSource dataSource;  // dataSource로 DB 접근 가능

    public void save(Author author) {
        try {
            Connection connection = dataSource.getConnection();
        } catch (SQLException e) {
            // unchecked 예외는 spring에서 트랜잭션 상황에서 롤백의 기준이 됨
            throw new RuntimeException(e);
        }
        return ;
    }

    public List<Author> findAll() {
        return (null);

    }

    public Optional<Author> findById(Long id) {
        return (null);

    }

    public Optional<Author> findByEmail(String email) {
        return (null);

    }

    public void delete(Long id) {
        return ;

    }
}
