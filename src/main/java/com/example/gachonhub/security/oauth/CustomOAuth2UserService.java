package com.example.gachonhub.security.oauth;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.User.Role;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import com.example.gachonhub.domain.user.userInfo.UserSns.SnsCategory;
import com.example.gachonhub.exception.OAuth2AuthenticationProcessingException;
import com.example.gachonhub.security.AppProperties;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AppProperties appProperties;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

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
            user = updateExistingUser(user, oAuth2UserInfo, oAuth2UserRequest);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, GithubOAuth2UserInfo oAuth2UserInfo) {
        Role role = (oAuth2UserInfo.getNickName().equals(appProperties.getGithub().getDeveloperId())) ?
                Role.ADMIN : Role.USER;

        User user = User.builder()
                .id(Long.parseLong(oAuth2UserInfo.getId()))
                .nickname(oAuth2UserInfo.getNickName())
                .name(oAuth2UserInfo.getName())
                .avatarUrl(oAuth2UserInfo.getImageUrl())
                .company(oAuth2UserInfo.getCompany())
                .graduate(true)
                .createdAt(oAuth2UserInfo.getCreatedAt())
                .description(oAuth2UserInfo.getBio())
                .role(role)
                .githubToken(oAuth2UserRequest.getAccessToken().getTokenValue())
                .build();

        UserSns userSns = saveBlog(user, oAuth2UserInfo);
        if (userSns != null) {
            user.getSns().add(userSns);
        }

        userRepository.save(user);

        return user;
    }


    @Transactional
    public User updateExistingUser(User existingUser, GithubOAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest) {
        log.info("Oauth2 => custom user service : updateExistingUser");

        existingUser.setAvatarUrl(oAuth2UserInfo.getImageUrl());
        existingUser.setCompany(oAuth2UserInfo.getCompany());
        existingUser.setDescription(oAuth2UserInfo.getBio());
        existingUser.setGithubToken(oAuth2UserRequest.getAccessToken().getTokenValue());

        return userRepository.save(existingUser);
    }
//
    private UserSns saveBlog(User user, GithubOAuth2UserInfo oAuth2UserInfo) {
        if (oAuth2UserInfo.getBlog() != null) {
            SnsCategory[] values = SnsCategory.values();

            for (SnsCategory n : values) {
                if (oAuth2UserInfo.getBlog().contains(n.getTitle())) {
                    UserSns sns = UserSns.builder()
                            .category(n.name())
                            .userId(user)
                            .url(oAuth2UserInfo.getBlog())
                            .auto(true)
                            .build();
                    return sns;
                }
            }
        }
        return null;
    }

}
