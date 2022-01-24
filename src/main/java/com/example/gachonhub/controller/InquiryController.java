package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import com.example.gachonhub.payload.request.InquiryRequestDto;
import com.example.gachonhub.payload.response.InquiryListResponseDto;
import com.example.gachonhub.payload.response.InquiryResponseDto;
import com.example.gachonhub.payload.response.ResponseUtil;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static com.example.gachonhub.payload.response.ResponseUtil.success;

@RestController
@RequiredArgsConstructor
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
                            @RequestBody @Validated({saveGroup.class, generalGroup.class}) InquiryRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException());
        inquiryService.savePost(user, dto);
        return success("문의글 저장 완료");

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteInquiry(@CurrentUser UserPrincipal userPrincipal, @RequestParam("postId") Long id) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException());
        inquiryService.deletePost(user, id);
        return success("문의글 삭제 완료");
    }

    @PutMapping
    public ResponseEntity<?> updateInquiry(@CurrentUser UserPrincipal userPrincipal,
                              @RequestBody @Validated({updateGroup.class, generalGroup.class}) InquiryRequestDto dto) throws IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException());
        inquiryService.updatePost(user, dto);
        return success("문의글 수정 완료");
    }
}
