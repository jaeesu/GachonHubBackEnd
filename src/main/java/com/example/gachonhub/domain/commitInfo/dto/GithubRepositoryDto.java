package com.example.gachonhub.domain.commitInfo.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubRepositoryDto {

    private Long id;
    private String name;
    private String full_name;
    private String description;
    private String html_url;
    private String visibility;
}
