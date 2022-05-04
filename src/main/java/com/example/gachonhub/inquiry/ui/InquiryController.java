package com.example.gachonhub.inquiry.ui;

import com.example.gachonhub.inquiry.application.InquiryService;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import com.example.gachonhub.inquiry.ui.dto.InquiryRequestDto;
import com.example.gachonhub.inquiry.ui.dto.InquiryListResponseDto;
import com.example.gachonhub.inquiry.ui.dto.InquiryResponseDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.example.gachonhub.common.ui.out.ResponseUtil.success;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> findAllInquiries(@RequestParam("page") int page) {
        InquiryListResponseDto allByPage = inquiryService.findAllByPage(page);
        return success(allByPage);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findInquiry(@PathVariable("postId") Long id) {
        InquiryResponseDto byId = inquiryService.findById(id);
        return success(byId);
    }

    @PostMapping
    public ResponseEntity<?> saveInquiry(@CurrentUser UserPrincipal userPrincipal,
                            @ModelAttribute @Validated({saveGroup.class, generalGroup.class}) InquiryRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        inquiryService.savePost(user, dto);
        return success("문의글 작성 완료");

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteInquiry(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long id) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        inquiryService.deletePost(user, id);
        return success("문의글 삭제 완료");
    }

    @PutMapping
    public ResponseEntity<?> updateInquiry(@CurrentUser UserPrincipal userPrincipal,
                              @ModelAttribute @Validated({updateGroup.class, generalGroup.class}) InquiryRequestDto dto) throws IllegalAccessException, IOException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        inquiryService.updatePost(user, dto);
        return success("문의글 수정 완료");
    }
}
