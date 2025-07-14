package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// Transaction하고 롤백에 대한 추가설명 필요
@Service    // 싱글톤 객체로 만들기 위한 어노테이션 사용
// 트랜잭션 처리가 없는 경우 -> @Component로 대체 가능
@RequiredArgsConstructor
// 비즈니스 로직 처리
// Controller에서 받은 요청을 처리하는 실제 로직이 작성되는 클래스
public class AuthorService {

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
    private final AuthorMemoryRepository authorMemoryRepository;

    // 객체조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
//        // 이메일 중복 검증
        if (authorMemoryRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        this.authorMemoryRepository.save(author);
    }

    public List<AuthorListDto> findAll() {
        List<Author> authorList = authorMemoryRepository.findAll();
        List<AuthorListDto> dtoList = new ArrayList<>();

        for (Author author : authorList) {
            dtoList.add(new AuthorListDto(author.getId(), author.getName(), author.getEmail()));
        }
        return (dtoList);
    }

    public AuthorDetailDto findById(Long id) throws NoSuchElementException{
        Author author = authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        AuthorDetailDto dto = new AuthorDetailDto(author.getId(), author.getName(), author.getEmail(), author.getPassword());

        return (dto);
    }



    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        // 기존의 password를 newPassword로 교체
        Author author = authorMemoryRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("없는 email입니다."));
        author.updatePw(authorUpdatePwDto.getPassword());
    }

    public void delete(Long id) {
        authorMemoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        // id만 던져놓고 레포지토리에서 리스트 삭제 처리
        authorMemoryRepository.delete(id);
    }

}
