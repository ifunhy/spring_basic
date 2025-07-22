package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    // post, author 두 개의 싱글톤 생성자 주입
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }



    public void save(PostCreateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // SCH안의 getContext에 Authentication 객체를 세팅
        String email = authentication.getName();    // 이름 == claims의 subject == email
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        // authorId의 존재 여부 검증 필요
        postRepository.save(dto.toEntity(author));  // author 객체를 넘겨줌
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 ID입니다."));
//        // 엔티티 간의 관계성 설정을 하지 않았을 때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
//        return (PostDetailDto.fromEntity(post, author));
        
        // 엔티티 간의 관계성 설정을 통해 Author객체를 쉽게 조회하는 경우
        return (PostDetailDto.fromEntity(post));    // post 안에 author가 들어가 있어서 post만 넘겨도 됨
    }

    public Page<PostListDto> findAll(Pageable pageable) {
        // 내 코드
//        return (postRepository.findAll().stream().map(PostListDto::fromEntity).collect(Collectors.toList()));

        // 강사님 코드
        // stream()으로 한줄 한줄 돌면서 조회
//        List<Post> postList = postRepository.findAll(); // 일반 전체 조회
//        List<Post> postList = postRepository.findAllJoin(); // 일반 inner join
//        List<Post> postList = postRepository.findAllFetchJoin();    // inner join fetch
        // postList를 조회할 때 참조관계에 있는 author까지 조회하게 되므로, N(author쿼리)+1(post쿼리)문제 발생
        // jpa는 기본방향성이 fetch lazy이므로, 참조하는 시점에 쿼리를 내보내게 되어, 직접 JOIN문을 만들어주지 않고, N+1문제 발생
//        return (postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList()));

//        // 페이지처리 findAll 호출
//        Page<Post> postList = postRepository.findAll(pageable);
////        return (postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList()));
//        return (postList.map(a -> PostListDto.fromEntity(a)));  // 리스트가 아니기 때문에 stream하면서 toList() 조회할 필요 없음
//        // a 객체를 꺼내면 PostList에서 찾아서 리턴해줄게?
        
        // 페이지처리 findAll 호출 + delYn 처리
        Page<Post> postList = postRepository.findAllByDelYn(pageable, "N");
//        return (postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList()));  // collect 로 형변환
        return (postList.map(a -> PostListDto.fromEntity(a)));  // 리턴타입이 리스트가 아니기 때문에 stream하면서 toList() 조회할 필요 없음

    }


}
