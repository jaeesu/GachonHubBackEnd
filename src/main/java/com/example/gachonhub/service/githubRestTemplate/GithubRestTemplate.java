package com.example.gachonhub.service.githubRestTemplate;

import com.example.gachonhub.domain.commitInfo.dto.CommitInfoDto;
import com.example.gachonhub.domain.commitInfo.dto.GithubOrganizationDto;
import com.example.gachonhub.domain.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.security.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
//@RequiredArgsConstructor
public class GithubRestTemplate {

    private final AppProperties appProperties;
    private final String baseuri;

    public GithubRestTemplate(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.baseuri = appProperties.getGithub().getBaseuri();
    }

    //all page? or 1?
    public List<GithubRepositoryDto> getUserGithubRepositories(User user) {
        //개인 repo(publi, private)의 main branch => ext : personal, orgs의 all branch
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<GithubRepositoryDto>> exchange = restTemplate.exchange( baseuri + "/user/repos", HttpMethod.GET, httpEntity(user.getGithubToken()), new ParameterizedTypeReference<List<GithubRepositoryDto>>() {
        });

        return exchange.getBody();
    }

    public List<CommitInfoDto> getRepositoryCommit(User user, String repos_full_name) {
        //getcommits

        RestTemplate restTemplate = new RestTemplate();
        String[] repos = repos_full_name.split("/");

        String uri = UriComponentsBuilder.fromUriString("/repos")
                .path("/{owner}")
                .path("/{repos}")
                .path("/commits")
                .buildAndExpand(repos[0], repos[1])
                .toUriString();

        ResponseEntity<List<CommitInfoDto>> exchange1 = restTemplate.exchange(baseuri + uri, HttpMethod.GET, httpEntity(user.getGithubToken()), new ParameterizedTypeReference<List<CommitInfoDto>>() {
        });
        List<CommitInfoDto> commitInfoList = exchange1.getBody();
        List<CommitInfoDto> authorCommitInfoList = commitInfoList.stream()
                .filter(x -> x.getCommit().getAuthor().getName().equals(user.getNickname()))
                .collect(Collectors.toList());
        return authorCommitInfoList;
    }

    public void getOrgs() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<GithubOrganizationDto>> exchange = restTemplate.exchange("https://api.github.com/user/orgs", HttpMethod.GET, httpEntity(" "), new ParameterizedTypeReference<List<GithubOrganizationDto>>() {
        });
        List<GithubOrganizationDto> body = exchange.getBody();
        for (int i = 0; i < body.size(); i++) {
            ResponseEntity<String> exchange1 = restTemplate.exchange("https://api.github.com/orgs/" + body.get(i).getLogin() + "/repos", HttpMethod.GET, httpEntity(" "), String.class);
            System.out.println(exchange1.getBody());
        }
    }

    public HttpEntity httpEntity(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "token " + token);
        return new HttpEntity(httpHeaders);
    }

}
