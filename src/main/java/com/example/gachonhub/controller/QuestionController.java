package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.example.gachonhub.payload.response.QuestionListResponseDto;
import com.example.gachonhub.payload.response.QuestionResponseDto;
import com.example.gachonhub.payload.response.ResponseUtil;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_USER_ID;

@Slf4j
@RestController
@RequestMapping("/api/posts/question")
@RequiredArgsConstructor
public class QuestionController {

    //질문 게시글을 삭제할 수 없다.

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> findAllQuestionPosts(@RequestParam("page") int page) {
        QuestionListResponseDto allQuestionPostsByPage = questionService.findAllQuestionPostsByPage(page);
        return ResponseUtil.success(allQuestionPostsByPage);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> findQuestionPost(@PathVariable("postId") Long id) {
        QuestionResponseDto questionPost = questionService.findQuestionPost(id);
        return ResponseUtil.success(questionPost);
    }

    @PostMapping
    public ResponseEntity<?> saveQuestionPost(@CurrentUser UserPrincipal userPrincipal,
                                              @ModelAttribute @Validated({generalGroup.class, saveGroup.class}) QuestionRequestDto questionRequestDto) throws IOException {
        //getid 가 null인 경우 에러
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        questionService.saveQuestionPost(user, questionRequestDto);
        return ResponseUtil.success("게시글 작성 완료");
        //Non-static method 'success(T)' cannot be referenced from a static context
    }

    @PutMapping
    public ResponseEntity<?> updateQuestionPost(@CurrentUser UserPrincipal userPrincipal,
                                                @ModelAttribute @Validated({updateGroup.class, generalGroup.class}) QuestionRequestDto questionRequestDto) throws IOException, IllegalAccessException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        questionService.updateQuestionPost(user, questionRequestDto);
        return ResponseUtil.success("게시글 수정 완료");
    }

}
