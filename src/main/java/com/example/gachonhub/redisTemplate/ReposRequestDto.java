package com.example.gachonhub.redisTemplate;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReposRequestDto {

    @Size(max = 3, message = "대표 레포지토리는 3개까지 지정할 수 있습니다.")
    @NotNull
    private List<Long> repos;


}
