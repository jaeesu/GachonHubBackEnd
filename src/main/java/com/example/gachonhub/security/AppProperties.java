package com.example.gachonhub.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "app")
@Component
@Getter
@Setter
public class AppProperties {

    //auth : 토큰 생성 비밀값, 토큰 만료시간
    //oauth2 : 인증디라이렉트uri 목록??

    private Auth auth = new Auth();
    private Github github = new Github();

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private long accessTokenExpirationMsec;
        private long refreshTokenExpirationMesc;
        private String authorizedRedirectUri;
    }

    @Getter
    @Setter
    public static class Github {
        private String developerId;
        private String developerPassword;
        private String baseuri;
    }

}
