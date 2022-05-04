package com.example.gachonhub.security;

import com.example.gachonhub.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserPrincipal implements OAuth2User, UserDetails {
    //userprincipal : security에서 지원하는 인증된 사용자의 안전한 정보??
    //oauth2user, userdetilas 등 다 알아보기
    private Long id;
    private String userName;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String nickName, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.userName = nickName;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        //인증된 user 생성 - role 지정가능

        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new UserPrincipal(
                user.getId(),
                user.getNickname(),
                authorities
        );
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        //인증된 user 생성 - attributes 지정 가능
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public String getPassword() {
        return null;
    }
}
