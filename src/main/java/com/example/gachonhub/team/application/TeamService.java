package com.example.gachonhub.team.application;

import com.example.gachonhub.commitInfo.dto.GithubOrganizationDto;
import com.example.gachonhub.redisTemplate.GithubReposService;
import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.team.domain.Team.TeamType;
import com.example.gachonhub.team.domain.TeamRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.user.domain.UserToTeam;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.team.ui.dto.TeamRequestDto;
import com.example.gachonhub.team.ui.dto.TeamListResponseDto;
import com.example.gachonhub.team.ui.dto.TeamResponseDto;
import com.example.gachonhub.redisTemplate.GithubRestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.example.gachonhub.common.exception.ErrorUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final GithubRestTemplate restTemplate;
    private final GithubReposService githubReposService;

    public TeamListResponseDto findAllTeamsByPage(int page, String type) {
        PageRequest pageRequest = PageRequest.of(page, 15, Sort.by("id").descending());
        Page<Team> teams = teamRepository.findAllByType(pageRequest, TeamType.valueOf(type));
        return TeamListResponseDto.fromPagable(teams);
    }

    public TeamResponseDto findTeam(Long id) {
        Team team = findTeamById(id);
        return TeamResponseDto.fromEntity(team);
    }

    //?????? ???????????? -> ?????? ?????? ????????? ????????????
    @Transactional
    public void saveTeam(User user, TeamRequestDto dto) {
        GithubOrganizationDto body = restTemplate.getOrgInfo(dto.getOrgName()).getBody();
        Team team = dto.toEntity(user, body.getReposUrl(), body.getAvatarUrl());
        teamRepository.save(team);
        addMember(user, user.getNickname(), team.getId());
    }

    @Transactional
    public void updateTeamInfo(User user, TeamRequestDto dto) {
        Team team = findTeamById(dto.getTeamId());
        isCorrectAuthor(user.getId(), team.getAuthorId());
        dto.updateTeam(team);
        teamRepository.save(team);
        githubReposService.updateGroupMainRepository(team, dto.getRepos());
    }

    @Transactional
    public void deleteTeam(User user, Long id) {
        Team team = findTeamById(id);
        isCorrectAuthor(user.getId(), team.getAuthorId());
        teamRepository.deleteById(id);
    }

    @Transactional
    public void addMember(User user, String userNickName, Long teamId) {
        Team team = findTeamById(teamId);
        isCorrectAuthor(user.getId(), team.getAuthorId());
        User newMember = userRepository.findByNickname(userNickName).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_NICKNAME));

        UserToTeam relation = UserToTeam.builder()
                .team(team)
                .user(newMember)
                .build();
        team.getUsers().add(relation);
        newMember.getGroups().add(relation);

    }

    @Transactional
    public void deleteMemmber(User user, String userNickName, Long teamId)  {
        Team team = findTeamById(teamId);
        isCorrectAuthor(user.getId(), team.getAuthorId());
        for (UserToTeam n : team.getUsers()){
            log.warn(n.getTeam().getId() + " " + n.getUser().getId());
        }

        UserToTeam relation = team.getUsers().stream()
                .filter(x -> x.getUser().getNickname().equals(userNickName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException());
        team.getUsers().remove(relation);
        user.getGroups().remove(relation);
    }

    public Team findTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP_ID));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));

    }

    public void isCorrectAuthor(Long userId, Long postAuthorId) {
        if (!userId.equals(postAuthorId)) {
            throw new NotAccessUserException(NOT_CORRECT_USER_ID);
        }
    }


}
