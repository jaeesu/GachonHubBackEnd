package com.example.gachonhub.service;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import com.example.gachonhub.payload.request.UserInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void updateUserProfile(User user, UserInfoRequestDto dto) {
        User user1 = dto.toEntity(user);
        dto.getSns().stream()
                .map(x -> UserSns.builder()
                        .id(x.getId())
                        .userId(user1)
                        .category(x.getCategory())
                        .url(x.getUrl())
                        .build()
                ).forEach(x -> user1.getSns().add(x));
        userRepository.save(user1);
    }


}
