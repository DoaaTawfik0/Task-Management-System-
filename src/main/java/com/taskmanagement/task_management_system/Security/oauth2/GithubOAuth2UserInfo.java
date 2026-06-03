package com.taskmanagement.task_management_system.Security.oauth2;

import java.util.Map;

public record GithubOAuth2UserInfo(Map<String, Object> attributes) implements OAuth2UserInfo {

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        // email not provided in GitHub
        return (String) attributes.get("email");
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("login");
    }

    @Override
    public String getLastName() {
        return "";
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}