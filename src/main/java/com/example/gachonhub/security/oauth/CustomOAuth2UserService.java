package com.example.gachonhub.security.oauth;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.User.Role;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.OAuth2AuthenticationProcessingException;
import com.example.gachonhub.security.AppProperties;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AppProperties appProperties;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        log.info("Oauth2 => custom user service : loadUser");
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        log.info("github token : " + oAuth2UserRequest.getAccessToken().getTokenValue());

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws OAuth2AuthenticationProcessingException {
//
        log.debug("Oauth2 => custom user service : processOAuth2User");

        //request의 정보를 들고와서 GithubOauth2Userinfo 만들기
        GithubOAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getId())) {
            throw new OAuth2AuthenticationProcessingException("Id not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByNickname(oAuth2UserInfo.getNickName());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!(oAuth2UserRequest.getClientRegistration().getRegistrationId()).equals(Utils.AuthProvider)) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        "github" + " account. Please use your " + "github" +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, GithubOAuth2UserInfo oAuth2UserInfo) {
        log.info("Oauth2 => custom user service : registerNewUser");
        log.info("request => " + oAuth2UserRequest.getAdditionalParameters().toString());

        Role role = (oAuth2UserInfo.getNickName().equals(appProperties.getGithub().getDeveloperId())) ?
                Role.ADMIN : Role.USER;

        log.debug("getnickname : {}", oAuth2UserInfo.getNickName() + "     " + appProperties.getGithub().getDeveloperId());
        User user = User.builder()
                .id(Long.parseLong(oAuth2UserInfo.getId()))
                .nickname(oAuth2UserInfo.getNickName())
                .name(oAuth2UserInfo.getName())
                .avatar_url(oAuth2UserInfo.getImageUrl())
                .company(oAuth2UserInfo.getCompany())
                .created_at(oAuth2UserInfo.getCreatedAt())
                .role(role)
                .githubToken(oAuth2UserRequest.getAccessToken().getTokenValue())
                .build();
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, GithubOAuth2UserInfo oAuth2UserInfo) {
        log.info("Oauth2 => custom user service : updateExistingUser");
//        existingUser.setName(oAuth2UserInfo.getName());
//        existingUser.setImgUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
