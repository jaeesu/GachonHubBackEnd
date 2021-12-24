package com.example.gachonhub.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Component
@Getter
public class AppProperties {

    //auth : 토큰 생성 비밀값, 토큰 만료시간
    //oauth2 : 인증디라이렉트uri 목록??

    private final Auth auth = new Auth();
    private final List<String> authorizedRedirectUris = new ArrayList<>();
//    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
    }

//    @Getter
//    public static final class OAuth2 {
//        private List<String> authorizedRedirectUris = new ArrayList<>();
//
//        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
//            this.authorizedRedirectUris = authorizedRedirectUris;
//            return this;
//        }
//    }

}
