package com.beyond.basic.b2_board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonDto {
    private Object result;  // body부에 들어가는 건데, 어떤 타입이 올 지 모르면 Object 세팅
    private int status_code;
    private String status_message;
}
