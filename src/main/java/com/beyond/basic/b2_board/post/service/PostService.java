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
        // authorId의 존재 여부 검증 필요
        postRepository.save(dto.toEntity());
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 ID입니다."));
        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        return (PostDetailDto.fromEntity(post, author));
    }

    public List<PostListDto> findAll() {
        // 내 코드
//        return (postRepository.findAll().stream().map(PostListDto::fromEntity).collect(Collectors.toList()));

        // 강사님 코드
        List<Post> postList = postRepository.findAll();
        return (postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList()));
    }

}
