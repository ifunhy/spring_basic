package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        LocalDateTime appointmentTime = null;   // 예약여부(N -> 예약x, Y-> 예약o, 시간정보필요)

        if (dto.getAppointment().equals("Y")) { // dto의 시간정보가 Y라면
            if (dto.getAppointmentTime() == null || dto.getAppointmentTime().isEmpty()) {
                throw new IllegalArgumentException("시간정보가 비워져 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(dto.getAppointmentTime(), dateTimeFormatter);   // 시간대 자르기
        }

        // authorId의 존재 여부 검증 필요
        postRepository.save(dto.toEntity(author, appointmentTime));  // author 객체를 넘겨줌
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 ID입니다."));
//        // 엔티티 간의 관계성 설정을 하지 않았을 때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
//        return (PostDetailDto.fromEntity(post, author));
        
        // 엔티티 간의 관계성 설정을 통해 Author객체를 쉽게 조회하는 경우
        return (PostDetailDto.fromEntity(post));    // post 안에 author가 들어가 있어서 post만 넘겨도 됨
    }

    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto dto) {
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

        // 검색을 위해 Specification 객체를 스프링에서 제공
        // 페이지처리 findAll 호출 + delYn 처리
        // dto에서 넘어온 객체를 specification 변수 선언하여 처리 -> new new Specification<Post>() 하고 오버라이딩 필요
        // specification객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로, 쿼리를 쉽게 생성
        // 검색할 객체, 페이징 처리를 위한 객체를 넘기면 spring에서 자동으로 처리
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // Root : Entity의 속성을 접근하기 위한 객체, CriteriaBuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();  // Predicate는 조건이라 생각하면 됨
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N")); // select * from post where del_yn="N"
                predicateList.add(criteriaBuilder.equal(root.get("appointment"), "N")); // and appointment="N"
                if (dto.getCategory() != null) {    // and category=inputCategory (동적)
                    predicateList.add(criteriaBuilder.equal(root.get("category"), dto.getCategory()));
                }
                if (dto.getTitle() != null) {   // and title like"%inputTitle%" (동적)
                    predicateList.add(criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%"));
                }
                Predicate[] predicateArr = new Predicate[predicateList.size()];

                for (int i = 0; i < predicateList.size(); i++) {
                    predicateArr[i] = predicateList.get(i);
                }

                // 위의 검색 조건들을 하나(한줄)의 Predicate형 객체로 만들어서 return
                Predicate predicate = criteriaBuilder.and(predicateArr);

                return (predicate);    // Predicate형 객체로 리턴
            }
        };
        Page<Post> postList = postRepository.findAll(specification ,pageable);  // 삭제되지 않고, 예약하지 않은 글만 조회
//        return (postList.stream().map(a -> PostListDto.fromEntity(a)).collect(Collectors.toList()));  // collect 로 형변환
        return (postList.map(a -> PostListDto.fromEntity(a)));  // 리턴타입이 리스트가 아니기 때문에 stream하면서 toList() 조회할 필요 없음
    }
}
