package com.example.gachonhub.notice.ui.dto;

import com.example.gachonhub.notice.domain.PostNotice;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponseDto {

    private Long id;
    private String user;
    private String title;
    private String content;
    private LocalDate writeAt;

    public static NoticeResponseDto fromNotice(PostNotice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .user(notice.getUserId().getNickname())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writeAt(notice.getWriteAt().toLocalDateTime().toLocalDate())
                .build();
    }

}
