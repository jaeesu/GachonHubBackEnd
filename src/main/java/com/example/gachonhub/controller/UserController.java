package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.payload.request.UserInfoRequestDto;
import com.example.gachonhub.payload.response.UserResponseDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.UserReposService;
import com.example.gachonhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.gachonhub.payload.response.ResponseUtil.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserReposService reposService;

    @GetMapping
    public ResponseEntity<?> getUserProfile(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException());
        UserResponseDto userResponseDto = UserResponseDto.fromEntity(user);
        return success(userResponseDto);
    }

    //학과, 재학여부, sns
    @PutMapping
    public ResponseEntity<?> updateUserSns(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid UserInfoRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException());
        userService.updateUserProfile(user, dto);
        return success("사용자 정보 수정 완료");
    }

    @PutMapping("/repos")
    public void updateUserRepos(@CurrentUser UserPrincipal userPrincipal, @RequestBody List<Integer> repos) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new NoSuchElementException());
        reposService.updateMainRepository(user, repos);
    }

}
