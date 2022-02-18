package com.example.gachonhub.payload.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class TeamContentRequestDto {

    @NotNull(message = "그룹 아이디가 누락되었습니다.")
    private Long teamId;

    @NotNull(message = "그룹 모집 내용이 누락되었습니다.")
    private String content;
}
