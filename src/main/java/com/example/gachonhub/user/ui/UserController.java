package com.example.gachonhub.user.ui;

import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.user.ui.dto.UserInfoRequestDto;
import com.example.gachonhub.user.ui.dto.UserResponseDto;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.redisTemplate.GithubReposService;
import com.example.gachonhub.user.application.UserService;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.gachonhub.common.ui.out.ResponseUtil.success;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final GithubReposService reposService;

    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestParam("id") Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        UserResponseDto userResponseDto = UserResponseDto.fromEntity(user);
        return success(userResponseDto);
    }

    //학과, 재학여부, sns
    @PutMapping
    public ResponseEntity<?> updateUserSns(@CurrentUser UserPrincipal userPrincipal, @RequestBody @Valid UserInfoRequestDto dto) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        userService.updateUserProfile(user, dto);
        reposService.updateUserMainRepository(user, dto.getRepos());
        return success("사용자 정보 수정 완료");
    }

}
