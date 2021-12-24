package com.example.gachonhub.security.oauth;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.OAuth2AuthenticationProcessingException;
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
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        log.info("Oauth2 => custom user service : loadUser");
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
        log.debug("Oauth2 => custom user service : processOAuth2User");
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        log.info(oAuth2UserInfo.getId() + "  " + oAuth2UserInfo.getEmail() + "  " + oAuth2UserInfo.getName() + "  " + oAuth2UserInfo.getAttributes());
        /**
         * email null error 처리 후 손보기
         */
        if (StringUtils.isEmpty(oAuth2UserInfo.getId())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
//        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail()) && StringUtils.isEmpty(oAuth2UserInfo.getName())) {
//            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
//        }

        Optional<User> userOptional = userRepository.findByName(oAuth2UserInfo.getAttributes().get("login").toString());
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

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        log.info("Oauth2 => custom user service : registerNewUser");
        log.info("request => " + oAuth2UserRequest.getAdditionalParameters().toString());
//        User user = new User();

        /**
         * email => null error (email로 로그인한 경우도 마찬가지)
         * 이미 존재하는 경우 잘 통과되는 것 확인
         */
        log.info("email : " + oAuth2UserInfo.getEmail());
        log.info("email2 :" + oAuth2UserInfo.getAttributes().toString());

        User user = User.builder()
                .id(Long.parseLong(oAuth2UserInfo.getId()))
                .email((oAuth2UserInfo.getEmail() == null) ? "test@email.com" : oAuth2UserInfo.getEmail())
                .name(oAuth2UserInfo.getAttributes().get("login").toString())
//                .password("hello")
                .build();
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        log.info("Oauth2 => custom user service : updateExistingUser");
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImgUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
