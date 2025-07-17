package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
// 조회용 DTO
// 조회 시 노출되는 정보를 제한하기 위해 사용 (Ex> password 숨기기)
public class AuthorListDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
