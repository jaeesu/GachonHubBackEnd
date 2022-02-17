package com.example.gachonhub.payload.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class TeamAddMemberRequestDto {

    @NotNull(message = "추가할 사용자 아이디가 누락되었습니다.")
    private Long memberId;

    @NotNull(message = "사용자를 추가할 팀 아이디가 누락되었습니다.")
    private Long teamId;
}
