package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
// 임시 저장소 (DB 대체)
// 회원 정보를 저장/조회/삭제하는 역할
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();    // 왜 static을 안 썼지? -> Spring에 의해 싱글톤으로 생성되기 때문

    public static Long id = 1L; // 고유 ID를 생성하기 위한 정적 변수

    public void save(Author author) {
        this.authorList.add(author);
        id++;
    }

    public List<Author> findAll() {
        return this.authorList;
    }

    public Optional<Author> findById(Long id) {
        Author author = null;
        for (Author a : this.authorList) {
            if (a.getId() == id) {
                author = a;
            }
        }
        return (Optional.ofNullable(author));
    }

    public Optional<Author> findByEmail(String email) {
        Author author = null;
        for (Author a : this.authorList) {
            if (a.getEmail().equals(email)) {
                author = a;
                return Optional.of((author));
            }
        }
        return (Optional.empty());
    }

    public void delete(Long id) {
        // id값으로 요소의 index값을 찾아 삭제
        for (int i = 0; i < this.authorList.size(); i++) {
            if (this.authorList.get(i).getId() == id) {
                this.authorList.remove(i);
                break;
            }
        }
    }


}

// private 필드를 다른 곳에서 사용할 수 있는 방법 (= private으로 두면서 객체를 받을 수 있는 방법)
// -> private 접근 제한자는 외부에서 직접 접근할 수 없다는 것을 의미하지만, Spring에서는 생성자 주입 또는 필드 주입으로 값을 전달받을 수 있음
// Ex> private이지만 DI 가능하여 적용됨 -> Spring이 자동으로 싱글톤 객체를 만들어서 넣어줌
// => 즉, private 이라도 Spring이 DI(의존성 주입)으로 넣어줄 수 있음
// 레포, 컨트롤러, 서비스는 "딱 한 번만 만들어짐" → 싱글톤

// 객체를 딱 한번만 만든다고 했을 때, 위처럼 구성하면 매번 리셋되는 게 아님. 클래스에서 만들어진 객체가 어딘가에 저장
// @Repository 어노테이션은 @Component가 구성되어 있어서 싱글톤으로 생성됨 -> 단 하나의 객체로 만들어짐
