package com.example.gachonhub.payload.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class TeamAddMemberRequestDto {

    private Long memberId;
    private Long teamId;
}
