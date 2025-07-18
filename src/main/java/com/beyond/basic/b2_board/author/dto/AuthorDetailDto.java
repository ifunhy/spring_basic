package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
// 조회용 DTO
// 조회 시 노출되는 정보를 제한하기 위해 사용 (Ex> password 숨기기)
public class AuthorDetailDto {
    private Long id;
    private String name;
    private String email;
    private Role role;  // 역할
    private Integer postCount; // 게시글 수
    private LocalDateTime createdTime;  // 생성시각
    
//    // 1개의 entity로만 dto가 조립되는 것이 아니기 때문에, dto계층에서 fromEntity를 설계
//    public static AuthorDetailDto fromEntity(Author author, Integer postCount) {
//            return (AuthorDetailDto.builder()
//                    .id(author.getId())
//            .name(author.getName())
//            .email(author.getEmail())
//            .role(author.getRole())
//            .postCount(postCount)
//                .createdTime(author.getCreatedTime())
//            .build());  // builder 패턴으로 수정
////        return (new AuthorDetailDto(author.getId(), author.getName(), author.getEmail(), author.getRole()));
//}


    // 매개변수의 postCount값 제거 후 변수 선언하여 내용 추가 또는 author.getPostList().size()
    public static AuthorDetailDto fromEntity(Author author) {
//        int postCount = author.getPostList().size();

        return (AuthorDetailDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .role(author.getRole())
//                .postCount(postCount)
                .postCount(author.getPostList().size())
                .createdTime(author.getCreatedTime())
                .build());  // builder 패턴으로 수정
//        return (new AuthorDetailDto(author.getId(), author.getName(), author.getEmail(), author.getRole()));
    }
}
