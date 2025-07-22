package com.beyond.basic.b2_board.common;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity   // PreAuthorized 어노테이션을 사용하기 위한 설정
public class SecurityConfig {

    // 내가 만든 객체는 @Component, 외부 라이브러리를 활용한 객체는 @Bean+ @Configuration
    // @Bean은 메소드 위에 붙여 return 되는 객체를 싱글톤 객체로 생성
    // @Component는 클래스 위에 붙여 클래스 자체를 싱글톤 객체로 생성
    // filter계층에서 filter로직을 커스텀
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return (httpSecurity
                // cors : 특정도메인에 대한 허용 정책, postman은 cors정책에 적용 X
                .cors(c -> c.configurationSource(corsConfiguration()))
                // csrf : 보안공격 중 하나로서 타 사이트의 쿠키값을 꺼내서 탈취하는 공격
                // 세션기반 로그인(mvc 패턴, ssr)에서는 csrf 별도 설정하는 것이 일반적
                // 토큰기반 로그인(rest api 서버, scr)에서는 csrf 별도 설정하지 않는 것이 일반적
                .csrf(AbstractHttpConfigurer::disable) // csrf 공격 -> 비활성화(토큰은 쿠키 미사용이라 대비하지 않음)
                // http basic : email/pw를 인코딩하여 인증(전송)하는 방식. 간단한 인증의 경우에만 사용
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션 로그인 방식 비활성화 -> 토큰인증 하겠다(SessionCreationPolicy.STATELESS)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // token을 검증하고, token검증을 통해 Authentication객체 생성
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 예외 api 정책 설정 -> 특정 url에 대해서는 인증처리하지 않겠다
                // authenticated() : 예외를 제외한 모든 요청에 대해서 Authentication객체가 생성되기를 요구
                // httpRequest 중에 author/create 랑 author/doLogin 말고는 다 인증처리할 건데, 그 기준은 authenticated 객체의 유무
                .authorizeHttpRequests(a -> a.requestMatchers("/author/create", "/author/doLogin").permitAll().anyRequest().authenticated())
                .build());
    }

    private CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));  // localhost:3000 말고는 불허
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP(get, post 등) 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더요소(Authorization 등) 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 url패턴에 대해 cors설정 적용
        return source;
    }
    // source를 configuration에 담아서 특정 도메인만 허용
    // 별 두 개 : 슬래시 안의 슬래시 안까지 이어지는 모든 것

    @Bean
    public PasswordEncoder passwordEncoder() {
        return (PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }
}
