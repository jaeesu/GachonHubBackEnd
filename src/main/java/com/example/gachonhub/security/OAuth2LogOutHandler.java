package com.example.gachonhub.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LogOutHandler extends SecurityContextLogoutHandler {

    //interface 상속 시 x

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("OAuth2LogOutHandler => logout");
        Long id = ((UserPrincipal) authentication.getPrincipal()).getId();
        redisTemplate.delete(String.valueOf(id));
    }
}
