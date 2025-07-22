package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.domain.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component  // 싱글톤으로 만듦, @Service로 하기도 함
public class JwtTokenProvider {

    @Value("${jwt.expirationAt}")   // yml 파일의 jwt.expirationAt 조건 주입
    private int expirationAt;   // yml에서 설정한 값을 가져옴

    @Value("${jwt.secretKeyAt}")    // yml 파일의 jwt.secretKeyAt 조건 주입
    private String secretKeyAt;

    private Key secret_at_key;

    public void makeKey() {
        // 인코딩 했다가 디코딩해서 꺼내 써야 함
        // java.util.Base64.getDecoder().decode(secretKeyAt) : yml에 넣은 인코딩한 키값이 들어감
        // SignatureAlgorithm.HS512.getJcaName() : 사용한 알고리즘 기술
        secret_at_key = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyAt)
                , SignatureAlgorithm.HS512.getJcaName());
    }

    public String createAtToken(Author author) {
        String email = author.getEmail();
        String role = author.getRole().toString();

        // Claims : payload(사용자정보)
        Claims claims = Jwts.claims().setSubject(email);    // setSubject(주된 값)
        // 주된 키값을 제외한 나머지 사용자 정보는 put 사용하여 key:value 세팅
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)  // 발행시간
                .setExpiration(new Date(now.getTime() + expirationAt*60*1000L))    // 완료시간(현재시간 기준 1000분 세팅(밀리초))
                // secretKey를 통해 signature 생성
                .signWith()
                .compact();
        return (token);
    }
}

// payload + serecKey를 조합하여 token을 만듦 -> 코드로 구현