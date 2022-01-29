package com.example.gachonhub.domain.commitInfo.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubOrganizationDto {
    private String login;
    private Long id;
    private String url;
}
