package com.example.gachonhub.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.util.Map;

@Getter
@AllArgsConstructor
public class GithubOAuth2UserInfo {

    private Map<String, Object> attributes;

    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    public String getNickName() {
        return (String) attributes.get("login");
    }

    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }

    public String getHomeUrl() {
        return (String) attributes.get("html_url");
    }

    public String getBlog() {
        return (String) attributes.get("blog");
    }

    public String getCompany() {
        return (String) attributes.get("company");
    }

    public String getCreatedAt() {
        return (String) attributes.get("created_at");
    }

    public String getUpdatedAt() {
        return (String) attributes.get("updated_at");
    }
}
