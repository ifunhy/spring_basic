package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// Transaction하고 롤백에 대한 추가설명 필요
@Service    // 싱글톤 객체로 만들기 위한 어노테이션 사용
@RequiredArgsConstructor    // 트랜잭션 처리가 없는 경우 -> @Component로 대체 가능
@Transactional  //  스프링에서 메소드 단위로 트랜잭션 처리를 하고, 만약 예외(unchecked)발생 시 자동 롤백처리 지원
public class AuthorService {    // Controller에서 받은 요청을 처리하는 실제 로직이 작성되는 클래스

//    // 의존성주입(DI)방법1. Autowired 어노테이션 사용 -> 필드 주입
//    // 싱글톤 객체를 내가 가져다 쓰겠다
//    @Autowired  // new authorRepository가 필요없음, final 키워드 사용 불가, 구조적으로 다형성 불가
//    private AuthorRepositoryInterface authorRepository;

//    // 의존성주입(DI)방법2. 생성자 주입(가장 많이 쓰는 방식)
//    // 장점 1) final을 통해 상수로 사용 가능(안정성 향상)
//    // 장점 2) 다형성 구현 가능
//    // 장점 3) 순환참조방식 (컴파일타임에 check, c/s/r이 서로 참조하는 것을 의미)
//    private final AuthorRepositoryInterface authorRepository;
//    // 객체로 만들어지는 시점에 스프링에서 authorRepository객체를 매개변수로 주입
//    // 생성자가 하나밖에 없을 때에는 Autowired 생략 가능
//    @Autowired    // 싱글톤 객체로 만들어진 것을 자동으로 주입해주는 기능
//    public AuthorService(AuthorMemoryRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }
    
    // 의존성주입(DI)방법3. RequiredArgsConstructor 어노테이션 사용
    // -> 반드시 초기화되어야 하는 필드(final 등)을 대상으로 생성자를 자동 생성, 다형성 설계는 불가
//    private final AuthorMemoryRepository authorMemoryRepository;
    private final AuthorJdbcRepository authorRepository;

    // 객체조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
//        // 이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) { // isPresent() : null 판단
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        // toEntity패턴을 통해 Author 객체 조립을 공통화
        Author author = authorCreateDto.authorToEntity();
        this.authorRepository.save(author);
    }

    // 트랜잭션이 필요없는 경우, 아래와 같이 명시적으로 제외
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
//        List<Author> authorList = authorMemoryRepository.findAll();
//
//        for (Author author : authorList) {
//            dtoList.add(new AuthorListDto(author.getId(), author.getName(), author.getEmail()));
//            AuthorListDto dto = author.listFromEntity();
//        }
//        return (dtoList);

        return (authorRepository.findAll().stream()
                .map(a -> a.listFromEntity()).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) throws NoSuchElementException{
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 ID입니다."));
//        AuthorDetailDto dto = new AuthorDetailDto(author.getId(), author.getName(), author.getEmail(), author.getPassword());
        AuthorDetailDto dto = author.detailFromEntity();
//        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        this.authorRepository.save(author);

        return (dto);
    }


    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        // 기존의 password를 newPassword로 교체
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("없는 email입니다."));
        author.updatePw(authorUpdatePwDto.getPassword());
    }

    public void delete(Long id) {
        authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        // id만 던져놓고 레포지토리에서 리스트 삭제 처리
        authorRepository.delete(id);
    }

}
