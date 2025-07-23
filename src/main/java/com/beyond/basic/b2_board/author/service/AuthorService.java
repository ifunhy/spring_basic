package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
//import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
//import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// Transaction하고 롤백에 대한 추가설명 필요
@Service    // 싱글톤 객체로 만들기 위한 어노테이션 사용
@RequiredArgsConstructor    // 트랜잭션 처리가 없는 경우 -> @Component로 대체 가능
@Transactional  //  스프링에서 메소드 단위로 트랜잭션처리(commit)를 하고, 만약 예외(unchecked)발생 시 자동 롤백처리 지원
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
//    private final AuthorJdbcRepository authorRepository;
//    private final AuthorJpaRepository authorRepository;
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;    // 암호화를 할 수 있는 클래스

    // 객체조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
//        // 이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()) { // isPresent() : null 판단
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }


//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        // toEntity패턴을 통해 Author 객체 조립을 공통화
        String encordedPassword = passwordEncoder.encode(authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity(encordedPassword);
//        this.authorRepository.save(author);  // cascading 테스트를 위한 주석처리
//        Author dbAuthor = this.authorRepository.save(author); // 저장하고 나서 db를 다시 조회한 값을 저장

        // cascading 테스트 : 회원이 생성될 때, 곧바로 "가입인사" 글을 생성하는 상황
        // 방법 2가지
        // 방법 1. 직접 POST객체 생성 후 저장
        Post post = Post.builder()
                .title("안녕하세요.")
                .contents(authorCreateDto.getName() + "입니다. 반갑습니다.")
                // author객체가 db에 save되는 순간 EntityManager와 영속성컨텍스트에 의해 author객체에도 id값 생성
                .author(author)
                .delYn("N")
                .build();
//        postRepository.save(post);
        // 방법 2. cascade옵션 활용
        author.getPostList().add(post);
        this.authorRepository.save(author);
    }

    // 로그인
    public Author doLogin(AuthorLoginDto dto) {
        Optional<Author> optionalAuthor = authorRepository.findByEmail(dto.getEmail());
        boolean check = true;

        if (!optionalAuthor.isPresent()) {
            check = false;
        } else {
            // 비밀번호 일치여부 검증 (각각 암호화 시킨 후 비교) : matches 함수를 통해 암호화되지 않은 값을 다시 암호화하여 DB의 password를 검증
            if (!passwordEncoder.matches(dto.getPassword(), optionalAuthor.get().getPassword())) {
                check = false;
            }
        }
        if (!check) {
            System.out.println("로그인 실패");
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }
        return (optionalAuthor.get());
    }

    // 트랜잭션이 필요없는 경우, 아래와 같이 명시적으로 제외 -> 성능적으로 유리
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll() {
        return (authorRepository.findAll().stream().map(a -> a.listFromEntity()).collect(Collectors.toList()));

//        List<Author> authorList = authorMemoryRepository.findAll();
//
//        for (Author author : authorList) {
//            dtoList.add(new AuthorListDto(author.getId(), author.getName(), author.getEmail()));
//            AuthorListDto dto = author.listFromEntity();
//        }
//        return (dtoList);

    }

//    @Transactional(readOnly = true)
//    public AuthorDetailDto findById(Long id) throws NoSuchElementException {
//        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 ID입니다."));
//
////        AuthorDetailDto dto = new AuthorDetailDto(author.getId(), author.getName(), author.getEmail(), author.getPassword());
////        AuthorDetailDto dto = author.detailFromEntity();
////        this.authorRepository.save(author);
//
//        // 연관관계 설정 없이 직접 조회해서 count값 찾는 경우
////        List<Post> postList = postRepository.findByAuthor(author);
////        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author, postList.size());
//
//        // OneToMany 설정을 통해 count값 찾는 경우
//          AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
//
//        return (dto);
//    }


    // 내 정보 조회
    @Transactional(readOnly = true)
    public AuthorDetailDto myinfo() throws NoSuchElementException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("없는 ID입니다."));
        AuthorDetailDto dto = AuthorDetailDto.fromEntity(author);
        return (dto);
    }

    //비밀번호 수정
    // 상단의 @Transactional 어노테이션으로 인해 자동으로 업데이트 됨
    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        // 기존의 password를 newPassword로 교체
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(() -> new NoSuchElementException("없는 email입니다."));
        // dirty checking : 객체를 수정한 후 별도의 update쿼리 발생시키지 않아도, 영속성 컨텍스트에 의해 객체 변경사항 자동 DB 반영
        author.updatePw(authorUpdatePwDto.getPassword());
    }

    // 회원 삭제
    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        // id만 던져놓고 레포지토리에서 리스트 삭제 처리
        authorRepository.delete(author);
    }



}
