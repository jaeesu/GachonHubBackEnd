package com.example.gachonhub.security.oauth;

import com.example.gachonhub.exception.OAuth2AuthenticationProcessingException;
import com.example.gachonhub.util.Utils;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        if (registrationId.equalsIgnoreCase(Utils.AuthProvider)) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }

    }
}
