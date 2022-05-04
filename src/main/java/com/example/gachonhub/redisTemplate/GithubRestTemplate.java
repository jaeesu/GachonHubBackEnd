package com.example.gachonhub.redisTemplate;

import com.example.gachonhub.commitInfo.dto.CommitInfoDto;
import com.example.gachonhub.commitInfo.dto.GithubOrganizationDto;
import com.example.gachonhub.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.security.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
//@RequiredArgsConstructor
public class GithubRestTemplate {

    private final AppProperties appProperties;
    private String baseuri;

    public GithubRestTemplate(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.baseuri = appProperties.getGithub().getBaseuri();
    }

    //all page? or 1?
    @Transactional
    public List<GithubRepositoryDto> getUserGithubRepositories(User user) {
        log.warn("repository get");
        //개인 repo(publi, private)의 main branch => ext : personal, orgs의 all branch
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<GithubRepositoryDto>> exchange = restTemplate.exchange( baseuri + "/user/repos", HttpMethod.GET, httpEntity(user.getGithubToken()), new ParameterizedTypeReference<List<GithubRepositoryDto>>() {
        });


        return exchange.getBody();
    }

    public List<CommitInfoDto> getRepositoryCommitByUser(User user, String repos_full_name) {
        return getRepositoryCommit(user, repos_full_name).stream()
                .filter(x -> x.getCommit().getAuthor().getName().equals(user.getNickname()))
                .collect(Collectors.toList());
    }

    public List<CommitInfoDto> getRepositoryCommit(User user, String repos_full_name) {
        //getcommits

        try {
            RestTemplate restTemplate = new RestTemplate();
            String[] repos = repos_full_name.split("/");
            String owner = repos[0];
            String repo = repos[1];

            String uri = UriComponentsBuilder.fromUriString("/repos")
                    .path("/{owner}")
                    .path("/{repos}")
                    .path("/commits")
                    .buildAndExpand(owner, repo)
                    .toUriString();

            ResponseEntity<List<CommitInfoDto>> exchange1 = restTemplate.exchange(baseuri + uri, HttpMethod.GET, httpEntity(user.getGithubToken()), new ParameterizedTypeReference<List<CommitInfoDto>>() {
            });
            List<CommitInfoDto> commitInfoList = exchange1.getBody();
            return commitInfoList;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public ResponseEntity<GithubOrganizationDto> getOrgInfo(String name) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GithubOrganizationDto> exchange = restTemplate.exchange("https://api.github.com/orgs/" + name,
                HttpMethod.GET, httpEntity(" "), new ParameterizedTypeReference<GithubOrganizationDto>() {
        });

        return exchange;
    }

    public List<GithubRepositoryDto> getOrgRepos(User user, String name) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<GithubRepositoryDto>> exchange = restTemplate.exchange("https://api.github.com/orgs/" + name + "/repos", HttpMethod.GET, httpEntity(user.getGithubToken()), new ParameterizedTypeReference<List<GithubRepositoryDto>>() {
            });
            List<GithubRepositoryDto> body = exchange.getBody();
            return body;
        } catch (HttpClientErrorException e){
            e.printStackTrace();
            return new ArrayList<>();
        }


    }

    public HttpEntity httpEntity(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "token " + token);
        return new HttpEntity(httpHeaders);
    }


}
