package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.ValidationGroups;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.request.ContestRequestDto;
import com.example.gachonhub.payload.response.ContestListResponseDto;
import com.example.gachonhub.payload.response.ContestResponseDto;
import com.example.gachonhub.payload.response.ResponseUtil;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.ContestService;
import com.example.gachonhub.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.gachonhub.payload.response.ResponseUtil.success;
import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/contest")
public class ContestController {

    private final UserRepository userRepository;
    private final ContestService contestService;

    @GetMapping
    public ResponseEntity<?> getAllContests(@RequestParam("page") int page) {
        ContestListResponseDto contestList = contestService.getContestList(page);
        return success(contestList);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getContest(@PathVariable("postId") Long id) {
        ContestResponseDto contest = contestService.getContest(id);
        return success(contest);
    }

    @PostMapping
    public void createContest(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Validated({saveGroup.class, generalGroup.class}) ContestRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        contestService.createContest(user, dto);
    }

    @PutMapping
    public void updateContest(@CurrentUser UserPrincipal userPrincipal, ContestRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        contestService.updateContest(user, dto);

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteContest(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long id) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        contestService.deleteContest(user, id);
        return success("공모전 글 수정 완료");
    }
}
