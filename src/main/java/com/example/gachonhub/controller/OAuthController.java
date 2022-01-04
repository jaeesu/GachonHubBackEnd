package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.security.TokenProvider;
import com.example.gachonhub.security.oauth.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final CustomUserDetailsService customUserDetailsService;

    private final TokenProvider tokenProvider;

    private final UserRepository userRepository;

    @GetMapping("/oauth")
    public void test(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        log.info("\n\ngithub oauth token => {}", token);
        response.sendRedirect("http://localhost:8080/ranking");

    }

    @GetMapping("/role")
    public String role(@RequestHeader("Authorization") String token , @AuthenticationPrincipal User user) {
        token = token.split(" ")[1];
        Long userIdFromToken = tokenProvider.getUserIdFromToken(token);
        Optional<User> byId = userRepository.findById(userIdFromToken);
        log.debug("token => " + User.Role.ADMIN.name());
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(byId.get().getNickname());
        return userDetails.getAuthorities().toString();
    }
}
