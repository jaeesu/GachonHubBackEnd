package com.example.gachonhub.domain.commitInfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class GithubOrganizationDto {

    @JsonProperty(value = "login")
    private String name;

    private Long id;

    @JsonProperty(value = "repos_url")
    private String reposUrl;
    @JsonProperty(value = "avatar_url")
    private String avatarUrl;

    @JsonProperty(value = "created_at")
    private String createdAt;

    @JsonProperty(value = "updated_at")
    private String updatedAt;
}
