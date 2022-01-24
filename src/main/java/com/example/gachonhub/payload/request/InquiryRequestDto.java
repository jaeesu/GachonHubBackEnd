package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.inquiry.PostInquiry;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.exception.BadRequestException;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryRequestDto {

    @NotNull(groups = {updateGroup.class}, message = "아이디가 누락되었습니다.")
    @Null(groups = {saveGroup.class}, message = "아이디를 명시할 수 없습니다.")
    private Long id;

    @NotNull(groups = {generalGroup.class}, message = "제목이 누락되었습니다.")
    private String title;

    @NotNull(groups = {generalGroup.class}, message = "내용이 누락되었습니다.")
    private String content;

    private MultipartFile file;

    private Integer password;

    @NotNull(groups = {generalGroup.class}, message = "비밀글 여부가 누락되었습니다.")
    private Boolean secret;

    public InquiryRequestDto(Long id, String title, String content, MultipartFile file, Integer password, Boolean secret) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.file = file;
        this.password = password;
        this.secret = secret;

        if ((secret != null) && (secret ^ (password != null))) {
            throw new BadRequestException("비밀번호 입력 값이 잘못되었습니다.");
        }
    }

    public PostInquiry toEntity(User user, byte[] bytes) {

        return PostInquiry.builder()
                .id(this.id)
                .userId(user)
                .title(this.title)
                .content(this.content)
                .img(bytes)
                .password(this.password)
                .secret(this.secret)
                .build();
    }
}
