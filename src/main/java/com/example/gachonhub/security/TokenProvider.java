package com.example.gachonhub.security;

import com.example.gachonhub.util.Utils.TokenType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import static com.example.gachonhub.util.Utils.TokenType.X_AUTH_TOKEN;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    private final AppProperties appProperties;

    public String createJwtToken(Authentication authentication, TokenType type) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer(type.getValue())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret());

        if (type.equals(X_AUTH_TOKEN)) {
            return jwtBuilder
                    .setSubject(Long.toString(userPrincipal.getId()))
                    .setExpiration(new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpirationMsec()))
                    .compact();
        } else {
            return jwtBuilder
                    .setExpiration(new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpirationMesc()))
                    .compact();
        }
    }

    //x-auth-token : 만료시에 verify 하지 않고, decode만 해서 id 가져오기

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        log.debug(claims.toString());

        return Long.parseLong(claims.getSubject());
    }


    //토큰의 유효성 확인
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //토큰의 유효성은 확인하기 않고, only decode
    public String decodeToken(String authToken) throws JsonProcessingException {
        String[] chunks = authToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap hashMap = objectMapper.readValue(payload, HashMap.class);
        String sub = String.valueOf(hashMap.get("sub"));
        return sub;
    }

}
