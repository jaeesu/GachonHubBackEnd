package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TeamListResponseDto {

    private int totalPages;
    private int page;
    private List<ListTeamDto> data;

    public static TeamListResponseDto fromPagable(Page<Team> teams) {
        return TeamListResponseDto.builder()
                .totalPages(teams.getTotalPages())
                .page(teams.getNumber())
                .data(teams.get()
                        .map(ListTeamDto::new)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    static class ListTeamDto {
        private Long id;
        private String name;
        private Long authorId;
        private String field;
        private String type;
        private String mainImage;
        private boolean recruiting;

        public ListTeamDto(Team team) {

            this.id = team.getId();
            this.name = team.getName();
            this.authorId = team.getAuthorId();
            this.field = team.getField();
            this.type = team.getType().name();
            this.mainImage = team.getMainImage();
            this.recruiting = team.isRecruiting();
        }

    }


}
