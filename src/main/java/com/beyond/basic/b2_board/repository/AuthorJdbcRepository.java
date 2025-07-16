//package com.beyond.basic.b2_board.repository;
//
//import com.beyond.basic.b2_board.domain.Author;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Repository // 싱글톤 객체로 만들기 위한 어노테이션
//public class AuthorJdbcRepository {
//
//    // JDBC는 raw 쿼리
//    // Datasource는 DB와 JDBC에서 사용하는 DB연결 드라이버 객체
//    // application.yml에 설정한 DB정보를 사용하여 dataSource객체 싱글톤 생성
//    @Autowired
//    private DataSource dataSource;  // dataSource로 DB 접근 가능
//
//    // jdbc의 단점
//    // 1. raw쿼리에서 오타가 나도 디버깅 어려움
//    // 2. 데이터 추가 시, 매개변수와 컬럼의 매핑을 수작업으로 해야 함
//    // 3. 데이터 조회 시, 객체 조립을 직접 해야 함
//    public void save(Author author) {
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "insert into author(name, email, password) values(?, ?, ?)";
//            // PreparedStatement객체로 만들어서 실행가능한 상태로 만드는 것
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, author.getName());
//            ps.setString(2, author.getEmail());
//            ps.setString(3, author.getPassword());  // name, email, password 꺼내서 ?, ?, ?에 직접 세팅해야 함
//            ps.executeUpdate(); // 추가, 수정의 경우는 executeUpdate, 조회는 executeQuery
//        } catch (SQLException e) {
//            // unchecked 예외는 spring에서 트랜잭션 상황에서 롤백의 기준이 됨
//            throw new RuntimeException(e);
//        }
//    }
//
//    public List<Author> findAll() {
//        List<Author> authorList = new ArrayList<>();
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "select * from author";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();    // 테이블 형태로 데이터가 들어옴
//            while (rs.next()) {    // 컬럼을 가리키던 rs가 값을 가리키게 됨 + 신규회원 값이면 null이 되므로 != null일 때 검증을 진행
//                Long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String password = rs.getString("password");
//                Author author = new Author(id, name, email, password);
//                authorList.add(author);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return (authorList);
//    }
//
//    public Optional<Author> findById(Long inputId) {
//        Author author = null;   // 초기값 세팅
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "select * from author where id = ?";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setLong(1, inputId);
//            ResultSet rs = ps.executeQuery();    // 테이블 형태로 데이터가 들어옴
//            if (rs.next()) {    // 컬럼을 가리키던 rs가 값을 가리키게 됨 + 신규회원 값이면 null이 되므로 != null일 때 검증을 진행
//                Long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String password = rs.getString("password");
//                author = new Author(id, name, email, password); // 있다면 정상적으로 author 객체에 값을 넣음
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return (Optional.ofNullable(author));   // 값이 있다면 author 리턴, 없다면 null 리턴
//    }
//
//    public Optional<Author> findByEmail(String inputEmail) {
//        Author author = null;
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "select * from author where email = ?";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, inputEmail);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {    // 신규회원 값이면 null이 되므로 != null일 때 검증을 진행
//                Long id = rs.getLong("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String password = rs.getString("password");
//                author = new Author(id, name, email, password);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return (Optional.ofNullable(author));
//    }
//
//    public void delete(Long id) {
//        try {
//            Connection connection = dataSource.getConnection();
//            String sql = "delete from author where id = ?";
//            PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setLong(1, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
