package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostDetailDto {
    private Long id;
    private String title;
    private String contents;
    private String authorEmail; // 조회 시 사용자의 id를 email로 보여주기 위함

    public static PostDetailDto fromEntity(Post post, Author author) {
        return (PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(author.getEmail())
                .build());
    }
}
