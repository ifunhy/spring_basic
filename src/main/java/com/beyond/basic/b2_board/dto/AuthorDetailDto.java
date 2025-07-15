package com.beyond.basic.b2_board.dto;

import com.beyond.basic.b2_board.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
// 조회용 DTO
// 조회 시 노출되는 정보를 제한하기 위해 사용 (Ex> password 숨기기)
public class AuthorDetailDto {
    private Long id;
    private String name;
    private String email;
    
    // 1개의 entity로만 dto가 조립되는 것이 아니기 때문에, dto계층에서 fromEntity를 설계
    public static AuthorDetailDto fromEntity(Author author) {
        return (new AuthorDetailDto(author.getId(), author.getName(), author.getEmail()));
    }
}
