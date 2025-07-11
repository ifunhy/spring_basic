package com.beyond.basic.b1_hello.controller;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data   // getter, setter, toString 메소드까지 모두 만들어주는 어노테이션
@AllArgsConstructor // 모든 매개변수가 있는 생성자
@NoArgsConstructor  // 기본생성자
// 기본생성자 + getter의 조합은 parsing이 이뤄지므로 보통은 필수적 요소
public class Student {
    private String name;
    private String email;
    private List<Score> scores;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class Score{
        private String subject;
        private int point;
    }
}
// {name:"test", email:"test@naver", scores:[{subject:"수학", point:80}, {subject:"영어", point:90}]};