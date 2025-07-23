package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostCreateDto {
    @NotEmpty
    private String title;
    private String contents;
//    @NotNull    // 숫자는 NotEmpty 불가
//    private Long authorId;     // Authentication 설정 이후 authorId 필요없음
    private String delYn;
    @Builder.Default
    private String appointment = "N";   // 예약여부의 Default는 N
    // 시간 정보는 직접 LocalDateTime으로 형변환하는 경우가 많음
    private String appointmentTime; // 예약시간

    public Post toEntity(Author author, LocalDateTime appointmentTime) {

        return (Post.builder()
                .title(this.title)
                .contents(this.contents)
//                .authorId(this.authorId)  // 관계성 설정 전
                .author(author) // 관계성 설정 후
                .appointment(this.appointment)
                .appointmentTime(appointmentTime)
                .delYn("N")     // default 값은 N
                .build());
    }
}
