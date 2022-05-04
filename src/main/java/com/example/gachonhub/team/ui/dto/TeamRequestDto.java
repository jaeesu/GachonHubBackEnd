package com.example.gachonhub.team.ui.dto;

import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.team.domain.Team.TeamType;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamRequestDto {


    @NotNull(groups = updateGroup.class, message = "그룹 아이디가 누락되었습니다.")
    @Null(groups = saveGroup.class, message = "그룹 아이디를 명시할 수 없습니다.")
    private Long teamId;

    @NotNull(groups = saveGroup.class, message = "그룹 이름이 누락되었습니다.")
    private String name;

    @NotNull(groups = generalGroup.class, message = "그룹 분야가 누락되었습니다.")
    private String field;

    @NotNull(groups = generalGroup.class, message = "그룹 인원이 누락되었습니다.")
    private Integer people;

    @NotNull(groups = saveGroup.class, message = "그룹 레포지토리가 누락되었습니다.")
    private String orgName;

    @NotNull(groups = generalGroup.class, message = "그룹 설명이 누락되었습니다.")
    private String description;

    @NotNull(groups = saveGroup.class, message = "그룹 형식이 누락되었습니다.")
    private TeamType type;

    @NotNull(groups = generalGroup.class, message = "그룹 팀원 모집 여부가 누락되었습니다.")
    private Boolean recruiting;

    @Null(groups = saveGroup.class, message = "그룹 모집글을 작성할 수 없습니다.")
    private String recruitingContent;

    @Size(max = 3, message = "대표 레포지토리는 3개까지 지정할 수 있습니다.")
    @Null(groups = saveGroup.class, message = "대표 레포지토리를 지정할 수 없습니다.")
    private List<Long> repos;

    public Team toEntity(User user, String repos, String url) {
        return Team.builder()
                .name(this.name)
                .authorId(user.getId())
                .field(this.field)
                .people(this.people)
                .repos(repos)
                .description(this.description)
                .type(this.type)
                .avatarUrl(url)
                .build();
    }

    public void updateTeam(Team team) {
        team.setField(this.field);
        team.setPeople(this.people);
        team.setDescription(this.description);
        team.setRecruiting(this.recruiting);
        team.setRecruitingContent(this.recruitingContent);
    }
    
}
