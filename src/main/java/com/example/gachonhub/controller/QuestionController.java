package com.example.gachonhub.controller;

import com.example.gachonhub.domain.question.QuestionRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.example.gachonhub.payload.response.ResponseUtil;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/posts/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final UserRepository userRepository;

    @GetMapping()
    public void findAllQuestionPosts(@RequestParam("page") int page) {
        questionService.findAllQuestionPostsByPage(page);

    }

    @GetMapping("/{postId}")
    public void findQuestionPost(@PathVariable("postId") Long id) {
        questionService.findQUestionById(id);
    }

    @PostMapping
    public ResponseEntity<?> saveQuestionPost(@CurrentUser UserPrincipal userPrincipal, @ModelAttribute @Valid QuestionRequestDto questionRequestDto) throws IOException {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException());
        questionService.saveQuestionPost(user, questionRequestDto);
        return ResponseUtil.success(questionRequestDto);
        //Non-static method 'success(T)' cannot be referenced from a static context
    }

    @PutMapping("/{postId}")
    public void updateQuestionPost(@CurrentUser UserPrincipal userPrincipal,
                                   @ModelAttribute @Valid QuestionRequestDto questionRequestDto, @PathVariable("postId") Long id) {

    }

    @DeleteMapping("/{postId}")
    public void deleteQuestionPost(@CurrentUser UserPrincipal userPrincipal, @PathVariable("postId") Long id) {
        questionRepository.deleteById(id);

    }
}
