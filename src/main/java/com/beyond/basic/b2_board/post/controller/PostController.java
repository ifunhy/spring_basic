package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.author.dto.CommonDto;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostCreateDto dto) {
        postService.save(dto);
        return (new ResponseEntity<>(new CommonDto("게시글 등록 완료", HttpStatus.CREATED.value(), "post is created"), HttpStatus.CREATED));
    }

    // 내 코드
//    @GetMapping("/list")
//    public List<PostListDto> postList() {
//        List<PostListDto> dto =postService.findAll();
//        return (dto);
//    }

    // 강사님 코드
    @GetMapping("/list")
    // 페이징처리를 위한 데이터 요청 형식 : localhost:8080/post/list -> default값이 페이지 객체 안으로 들어감
    // 페이징처리를 위한 데이터 요청 형식 : localhost:8080/post/list?page=0&size=20&sort=title,asc -> 파라미터를 받아주는 형식으로 들어감
    // 요청을 ?& 라고 호출하고 있기 때문에 파라미터를 받아주는 형식인 것
    public ResponseEntity<?> postList(@PageableDefault(size=10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)  {
        Page<PostListDto> dto =postService.findAll(pageable);
        return (new ResponseEntity<>(new CommonDto(dto, HttpStatus.OK.value(),"OK"), HttpStatus.OK));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        PostDetailDto dto = postService.findById(id);
        return (new ResponseEntity<>(new CommonDto(dto, HttpStatus.OK.value(), "post is found"), HttpStatus.OK));    }
}
