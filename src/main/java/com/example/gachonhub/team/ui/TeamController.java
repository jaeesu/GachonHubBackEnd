package com.example.gachonhub.team.ui;

import com.example.gachonhub.team.application.TeamService;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import com.example.gachonhub.team.ui.dto.TeamRequestDto;
import com.example.gachonhub.team.ui.dto.TeamListResponseDto;
import com.example.gachonhub.team.ui.dto.TeamResponseDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.gachonhub.common.ui.out.ResponseUtil.success;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class TeamController {

    //스터디 : 그룹이름, 공부분야, 사용언어, 모집인원, 레포지토리 주소, 모집 여부, 모집 세부 사항
    //동아리 : 동아리 이름, 공부분야, 현재인원, 레포지토리 주소, 대표 이미지, 모집 세부 사항
    //동아리 소개 및 스터디 소개 => master 브랜치의 readme.md

    private final UserRepository userRepository;
    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<?> getAllTeams(@RequestParam("page") int page, @RequestParam("type") String type) {
        TeamListResponseDto teams = teamService.findAllTeamsByPage(page, type);
        return success(teams);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getTeamById(@PathVariable("groupId") Long id) {
        TeamResponseDto team = teamService.findTeam(id);
        return success(team);
    }

    @PostMapping
    public ResponseEntity<?> makeTeam(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Validated({saveGroup.class, generalGroup.class}) TeamRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        teamService.saveTeam(user, dto);
        return success("팀 생성 성공");
    }

    @PutMapping
    public ResponseEntity<?> updateTeamInfo(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Validated({updateGroup.class, generalGroup.class}) TeamRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        teamService.updateTeamInfo(user, dto);
        return success("팀 정보 수정 완료");
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteTeam(@CurrentUser UserPrincipal userPrincipal, @PathVariable("groupId") Long id) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        teamService.deleteTeam(user, id); //멤버 한 명도 없을 때 삭제 가능
        return success("팀 삭제 성공");
    }

    @GetMapping("/member")
    public ResponseEntity<?> addMember(@CurrentUser UserPrincipal userPrincipal, @RequestParam("user") String userNickName, @RequestParam("team") Long teamId) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        teamService.addMember(user, userNickName, teamId);
        return success("팀 멤버 추가 완료");
    }

    @DeleteMapping("/member")
    public ResponseEntity<?> removeMember(@CurrentUser UserPrincipal userPrincipal, @RequestParam("user") String userNickName, @RequestParam("team") Long teamId)  {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        teamService.deleteMemmber(user, userNickName, teamId);
        return success("팀 멤버 삭제 완료");
    }

}
