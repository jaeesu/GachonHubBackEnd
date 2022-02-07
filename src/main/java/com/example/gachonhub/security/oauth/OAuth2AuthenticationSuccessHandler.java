package com.example.gachonhub.security.oauth;

import com.example.gachonhub.exception.BadRequestException;
import com.example.gachonhub.redisTemplate.RedisCustomTemplate;
import com.example.gachonhub.security.AppProperties;
import com.example.gachonhub.security.CookieUtils;
import com.example.gachonhub.security.TokenProvider;
import com.example.gachonhub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.gachonhub.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.example.gachonhub.util.Utils.TokenType.AUTHORIZATION;
import static com.example.gachonhub.util.Utils.TokenType.REFRESH;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    private final AppProperties appProperties;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final RedisCustomTemplate redisCustomTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        /** access token + refresh token 생성 및 전송 */
        //access token 생성
        String accessToken = tokenProvider.createJwtToken(authentication, AUTHORIZATION);

        //refresh token 생성 & redis 저장
        String refreshToken = tokenProvider.createJwtToken(authentication, REFRESH);
        redisCustomTemplate.setRedisTokenFullValue(
                String.valueOf(((UserPrincipal) authentication.getPrincipal()).getId()),
                refreshToken, 999999999999L, TimeUnit.MICROSECONDS); //시간 후 자동 삭제
        log.debug("token redis 저장 : 만료시간 저장");
        //token 정보 다시 저장

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam(AUTHORIZATION.getValue(), accessToken)
                .queryParam(REFRESH.getValue(), refreshToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        log.info("success => isAuthorizedRedirectUri");
        URI clientRedirectUri = URI.create(uri);

        URI authorizedURI = URI.create(appProperties.getAuth().getAuthorizedRedirectUri());
        if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                && authorizedURI.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }
}
