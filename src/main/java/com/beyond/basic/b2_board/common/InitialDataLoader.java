package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// CommandLineRunner를 구현함으로써 해당 컴포넌트가 스프링빈(싱글톤객체)으로 등록되는 시점에 run메소드 자동 실행
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;  // password 암호화 위해 의존성 주입

    @Override
    public void run(String... args) throws Exception {

        if (authorRepository.findByEmail("admin@naver.com").isPresent()) {
            return ;    // 실행 시 repository에 admin 계정이 있다면 그냥 return
        }

        Author author = Author.builder()
                            .email("admin@naver.com")
                            .role(Role.ADMIN)
                            .password(passwordEncoder.encode("12341234")) // 암호화
                            .build();
        authorRepository.save(author);
    }
}
