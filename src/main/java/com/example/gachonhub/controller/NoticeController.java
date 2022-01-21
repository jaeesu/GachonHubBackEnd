package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.payload.ValidationGroups;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import com.example.gachonhub.payload.request.NoticeRequestDto;
import com.example.gachonhub.payload.response.NoticeListResponseDto;
import com.example.gachonhub.payload.response.NoticeResponseDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static com.example.gachonhub.payload.response.ResponseUtil.success;

@RestController
@RequestMapping("/api/posts/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final UserRepository userRepository;
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<?> findAllNoticePosts(@RequestParam("page") int page) {
        NoticeListResponseDto dto = noticeService.findAllNoticePostsByPage(page);
        return success(dto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findNoticePost(@PathVariable("postId") Long id) {
        NoticeResponseDto noticePost = noticeService.findNoticePost(id);
        return success(noticePost);
    }

    @PostMapping
    public ResponseEntity<?> saveNoticePost(@CurrentUser UserPrincipal userPrincipal,
                                            @RequestBody @Validated({generalGroup.class, saveGroup.class}) NoticeRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException("사용자 인증이 필요합니다."));
        noticeService.saveNoticePost(user, dto);
        return success("공지사항 저장 완료");
    }

    @DeleteMapping ("/{postId}")
    public ResponseEntity<?> deleteNoticePost(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long id) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException("사용자 인증이 필요합니다."));
        noticeService.deleteNoticePost(user, id);
        return success("공지사항 삭제 완료");
    }

    @PutMapping
    public ResponseEntity<?> updateNoticePost(@CurrentUser UserPrincipal userPrincipal,
                                              @RequestBody @Validated({generalGroup.class, updateGroup.class}) NoticeRequestDto dto) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException("사용자 인증이 필요합니다."));
        noticeService.updateNoticePost(user, dto);
        return success("곧지사항 수정 완료");
    }

}
