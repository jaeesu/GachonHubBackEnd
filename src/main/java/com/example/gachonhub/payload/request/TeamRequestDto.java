package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.team.Team;
import com.example.gachonhub.domain.team.Team.TeamType;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamRequestDto {


    @NotNull(groups = updateGroup.class, message = "그룹 아이디가 누락되었습니다.")
    @Null(groups = saveGroup.class, message = "그룹 아이디를 명시할 수 없습니다.")
    private Long teamId;

    @NotNull(groups = generalGroup.class, message = "그룹 이름이 누락되었습니다.")
    private String name;

    @NotNull(groups = generalGroup.class, message = "그룹 분야가 누락되었습니다.")
    private String field;

    @NotNull(groups = generalGroup.class, message = "그룹 인원이 누락되었습니다.")
    private Integer people;

    @NotNull(groups = generalGroup.class, message = "그룹 레포지토리가 누락되었습니다.")
    private String repos;

    @NotNull(groups = generalGroup.class, message = "그룹 설명이 누락되었습니다.")
    private String description;

    @NotNull(groups = generalGroup.class, message = "그룹 형식이 누락되었습니다.")
    private TeamType type;

    private MultipartFile image;

    public Team toEntity(User user, String url) {
        return Team.builder()
                .name(this.name)
                .authorId(user.getId())
                .field(this.field)
                .people(this.people)
                .repos(this.repos)
                .description(this.description)
                .type(this.type)
                .mainImage(url)
                .build();
    }
    
}
