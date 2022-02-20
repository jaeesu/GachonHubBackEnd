package com.example.gachonhub.service;

import com.example.gachonhub.domain.team.Team;
import com.example.gachonhub.domain.team.Team.TeamType;
import com.example.gachonhub.domain.team.TeamRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.relation.UserToTeam;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.TeamAddMemberRequestDto;
import com.example.gachonhub.payload.request.TeamContentRequestDto;
import com.example.gachonhub.payload.request.TeamRequestDto;
import com.example.gachonhub.payload.response.TeamListResponseDto;
import com.example.gachonhub.payload.response.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.example.gachonhub.util.ErrorUtil.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final AmazonS3Service s3Service;

    public TeamListResponseDto findAllTeamsByPage(int page, String type) {
        PageRequest pageRequest = PageRequest.of(page, 15, Sort.by("id").descending());
        Page<Team> teams = teamRepository.findAllByType(pageRequest, TeamType.valueOf(type));
        return TeamListResponseDto.fromPagable(teams);
    }

    public TeamResponseDto findTeam(Long id) {
        Team team = findTeamById(id);
        return TeamResponseDto.fromEntity(team);
    }

    //그룹 저장하고 -> 해당 사람 그룹에 추가하기
    @Transactional
    public void saveTeam(User user, TeamRequestDto dto) throws IllegalAccessException {
        String url = (dto.getImage() != null) ? s3Service.uploadFile(dto.getImage()) : null;
        Team team = dto.toEntity(user, url);
        teamRepository.save(team);
        addMember(user, new TeamAddMemberRequestDto(user.getId(), team.getId()));
    }

    @Transactional
    public void updateTeamInfo(User user, TeamRequestDto dto) throws IllegalAccessException {
        Team team = findTeamById(dto.getTeamId());
        isCorrectAuthor(user.getId(), team.getAuthorId());
        saveTeam(user, dto);
    }

    @Transactional
    public void deleteTeam(User user, Long id) throws IllegalAccessException {
        Team team = findTeamById(id);
        isCorrectAuthor(user.getId(), team.getAuthorId());

        UserToTeam userToTeam = team.getUsers().stream()
                .filter(x -> x.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
        team.getUsers().remove(userToTeam);
        user.getGroups().remove(userToTeam);
    }

    @Transactional
    public void addMember(User user, TeamAddMemberRequestDto dto) throws IllegalAccessException {
        Team team = findTeamById(dto.getTeamId());
        isCorrectAuthor(user.getId(), team.getAuthorId());
        User newMember = findUserById(dto.getMemberId());

        UserToTeam relation = UserToTeam.builder()
                .team(team)
                .user(newMember)
                .build();
        team.getUsers().add(relation);
        newMember.getGroups().add(relation);

    }

    @Transactional
    public void deleteMemmber(User user, TeamAddMemberRequestDto dto)  {
        Team team = findTeamById(dto.getTeamId());
        isCorrectAuthor(user.getId(), team.getAuthorId());

        UserToTeam relation = team.getUsers().stream()
                .filter(x -> x.getUser() == user)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException());
        team.getUsers().remove(relation);
        user.getGroups().remove(relation);
    }

    public void updateRecruitingContent(User user, TeamContentRequestDto dto) {
        Team team = findTeamById(dto.getTeamId());
        isCorrectAuthor(user.getId(), team.getAuthorId());
        team.updateContent(dto.getContent());
    }

    public void changeRecruitingStatus(User user, Long id) {
        Team team = findTeamById(id);
        isCorrectAuthor(user.getId(), team.getAuthorId());
        team.changeRecruiting();
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
