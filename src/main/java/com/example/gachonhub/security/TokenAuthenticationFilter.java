package com.example.gachonhub.security;

import com.example.gachonhub.redisTemplate.RedisCustomTemplate;
import com.example.gachonhub.security.oauth.CustomUserDetailsService;
import com.example.gachonhub.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.gachonhub.util.Utils.TokenType.X_AUTH_TOKEN;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    public TokenAuthenticationFilter() {
//    }
//
//    @Autowired
//    public TokenAuthenticationFilter(TokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
//        this.tokenProvider = tokenProvider;
//        this.customUserDetailsService = customUserDetailsService;
//    }

    /**
     * refresh token 만료 - access token 살아 있음 인증 가능
     *                   - access token 만료 - 다시 인증
     * => 만료 또는 로그아웃 시에, refresh token은 삭제
     *
     * refresh token 살아 있음 - access token 살아 있음 - 그대로 진행
     *                       - access token 만료 - 재발급
     */

    //토큰 내부 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("TokenAuthenticationFilter => doFtilerInternal");
        try {

            /**
             * 로직 고민 - 재발급을 위한 로직
             */
            //access 토큰 확인
            String accessToken = getJwtFromRequest(request, X_AUTH_TOKEN.getValue());

            //유효한 access 토큰
            if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
                Long userId = tokenProvider.getUserIdFromToken(accessToken);

                //refresh token의 유효성 검사...? token이 만료되면 알아서 삭제되게 할 수 있을까?
                if (redisTemplate.hasKey(String.valueOf(userId))) {
                    UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //유효한 jwt 토큰일때
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    //토큰 parsing
    private String getJwtFromRequest(HttpServletRequest request, String tokenHeader) {
        log.debug("TokenAuthenticationFilter => token parsing");
        String bearerToken = request.getHeader(tokenHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
