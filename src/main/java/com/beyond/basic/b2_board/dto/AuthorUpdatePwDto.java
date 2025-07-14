package com.beyond.basic.b2_board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// 비밀번호 수정 요청 DTO
public class AuthorUpdatePwDto {
    private String email;
    private String password;
}
