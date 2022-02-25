package com.example.gachonhub.service;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import com.example.gachonhub.payload.request.UserInfoRequestDto;
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
