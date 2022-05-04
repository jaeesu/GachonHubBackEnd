package com.example.gachonhub.contest.ui;

import com.example.gachonhub.contest.application.ContestService;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import com.example.gachonhub.contest.ui.dto.ContestRequestDto;
import com.example.gachonhub.contest.ui.dto.ContestListResponseDto;
import com.example.gachonhub.contest.ui.dto.ContestResponseDto;
import com.example.gachonhub.contest.ui.dto.ContestSimpleResponseDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.gachonhub.common.ui.out.ResponseUtil.success;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/contest")
public class ContestController {

    private final ContestService contestService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getContestList(@RequestParam("page") int page) {
        ContestListResponseDto contestList = contestService.getContestList(page);
        return success(contestList);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getContest(@PathVariable("postId") Long id) {
        ContestResponseDto contest = contestService.getContest(id);
        return success(contest);
    }

    @PostMapping
    public ResponseEntity<?> createContest(@CurrentUser UserPrincipal userPrincipal, @ModelAttribute @Validated({saveGroup.class, generalGroup.class}) ContestRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        contestService.createContest(user, dto);
        return success("공모전 글 작성 완료");
    }

    @PutMapping
    public ResponseEntity<?> updateContest(@CurrentUser UserPrincipal userPrincipal, @ModelAttribute @Validated({updateGroup.class, generalGroup.class}) ContestRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        contestService.updateContest(user, dto);
        return success("공모전 글 수정 완료");
    }

    @DeleteMapping("/{post")
    public ResponseEntity<?> deleteContest(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long id) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        contestService.deleteContest(user, id);
        return success("공모전 글 삭제 완료");
    }

    @GetMapping("/list/{postId}")
    public ResponseEntity<?> getListById(@PathVariable("postId") Long id) {
        List<ContestSimpleResponseDto> listById = contestService.getListById(id);
        return success(listById);
    }




}
