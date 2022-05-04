package com.example.gachonhub.inquiry.ui.dto;

import com.example.gachonhub.inquiry.domain.PostInquiry;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryResponseDto {

    private Long id;
    private String user;
    private String title;
    private String content;
    private String img;
    private LocalDate writeAt;
    private Integer password;
    private boolean secret;

    public static InquiryResponseDto fromInquiry(PostInquiry inquiry) {
        return InquiryResponseDto.builder()
                .id(inquiry.getId())
                .user(inquiry.getUserId().getNickname())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .img(inquiry.getImgUrl())
                .writeAt(inquiry.getCreatedAt().toLocalDate())
                .password(inquiry.getPassword())
                .secret(inquiry.isSecret())
                .build();
    }

}
