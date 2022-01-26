package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.LikesRequestDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.gachonhub.payload.response.ResponseUtil.success;
import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesController {
    //댓글 : 문의 사항 게시글, 질문 게시글
    //질문 게시글, 질문 게시글의 댓글, 공모전 게시글
    //댓글에 상위 댓글이 없다면 0, 있다면 댓글 번호

    private final LikesService likesService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> saveLikes(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid LikesRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        likesService.saveLikes(user, dto);
        return success("좋아요 반응 추가 완료");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLikes(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid LikesRequestDto dto) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        likesService.deleteLikes(user, dto);
        return success("좋아요 반응 삭제 완료");
    }

}
