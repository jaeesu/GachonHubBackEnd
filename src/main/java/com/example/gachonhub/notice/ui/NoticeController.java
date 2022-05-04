package com.example.gachonhub.notice.ui;

import com.example.gachonhub.notice.application.NoticeService;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import com.example.gachonhub.notice.ui.dto.NoticeRequestDto;
import com.example.gachonhub.notice.ui.dto.NoticeListResponseDto;
import com.example.gachonhub.notice.ui.dto.NoticeResponseDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.gachonhub.common.ui.out.ResponseUtil.success;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_USER_ID;

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
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        noticeService.saveNoticePost(user, dto);
        return success("공지사항 저장 완료");
    }

    @DeleteMapping ("/{postId}")
    public ResponseEntity<?> deleteNoticePost(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long id)  {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        noticeService.deleteNoticePost(user, id);
        return success("공지사항 삭제 완료");
    }

    @PutMapping
    public ResponseEntity<?> updateNoticePost(@CurrentUser UserPrincipal userPrincipal,
                                              @RequestBody @Validated({generalGroup.class, updateGroup.class}) NoticeRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        noticeService.updateNoticePost(user, dto);
        return success("곧지사항 수정 완료");
    }

}
