package com.example.gachonhub.user.application;

import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.ui.dto.UserInfoRequestDto;
import com.example.gachonhub.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void updateUserProfile(User user, UserInfoRequestDto dto) {
        dto.toEntity(user, dto);
        userRepository.save(user);
    }

}
