package com.example.gachonhub.security.oauth;

import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    //userprincipal - 인증 및 권한 부여
    //@currentuser 애노테이션 현재 로그인한 사용자에 접근

    private final UserRepository userRepository;

    //사용자를 이름으로 불러와서 userprincipal user로 생성
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        log.debug("custom user service => loaduserByUserName");
        User user = userRepository.findByNickname(name).orElseThrow(
                () -> new UsernameNotFoundException("User not found with name : " + name)
        );

        return UserPrincipal.create(user);
    }

    //사용자를 아이디로 불러와서 userprincipal user로 생성
    @Transactional
    public UserDetails loadUserById(Long id) {
        log.debug("custom user service => loaduserByUserId");
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("해당 아이디의 사용자가 존재하지 않습니다.")
        );

        return UserPrincipal.create(user);
    }
}
