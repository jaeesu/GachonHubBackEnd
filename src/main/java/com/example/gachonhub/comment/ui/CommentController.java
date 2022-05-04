package com.example.gachonhub.comment.ui;

import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import com.example.gachonhub.common.ui.in.CommentRequestDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.comment.application.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.gachonhub.common.ui.out.ResponseUtil.success;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> saveComment(@CurrentUser UserPrincipal userPrincipal,
                                      @RequestBody @Validated({saveGroup.class, generalGroup.class}) CommentRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        commentService.saveComment(user, dto);
        return success("댓글 작성 완료");
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@CurrentUser UserPrincipal userPrincipal,
                              @RequestBody @Validated({updateGroup.class, generalGroup.class}) CommentRequestDto dto) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        commentService.updateComment(user, dto);
        return success("댓글 수정 완료");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@CurrentUser UserPrincipal userPrincipal, @PathVariable("commentId") Long id) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        commentService.deleteComment(user, id);
        return success("댓글 삭제 완료");
    }
}
